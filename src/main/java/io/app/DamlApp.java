package io.app;

import io.beans.DamlLedger;
import io.github.renegrob.infinispan.embedded.CacheService;
import io.model.cache.DamlContractEntry;
import io.model.daml.DAMLFetchResponse;
import io.model.daml.template.Fetch;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.quarkus.vertx.ConsumeEvent;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import lombok.extern.slf4j.Slf4j;

@ClientEndpoint
@ApplicationScoped
@Slf4j
public class DamlApp extends Endpoint/* implements ICacheServiceEventListener */ {

    @Inject
    private CamelContext camelContext;

    @Inject
    private DamlLedger ledger;

    @Inject
    private CacheService cacheService;

    private Session session;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private WebSocketContainer container;
    private ClientEndpointConfig config;

    @ConfigProperty(name = "daml.ws.uri")
    private String URI;

    @ConfigProperty(name = "daml.ws.protocol")
    private String PROTOCOL;

    // PostConstruct는 최초 1회만 실행됨, reload인경우 다시 실행안됨
    // @ConsumeEvent
    // public void init() {
    // log.info("1. ADD Listener");
    // cacheService.addEventListener(this);
    // }

    // @Override
    // public void onCacheServiceNext() {
    // // CacheService의 next() 호출 시 초기화 로직
    // initialize();
    // }

    @ConsumeEvent(value = "daml-client-initialize", blocking = true)
    public void initialize(String event) throws Exception {
        log.info("2. START DamlApp initialize");
        createConfig();
        scheduler.scheduleAtFixedRate(this::checkConnection, 0, 3, TimeUnit.SECONDS);
    }

    // public void onStart(@Observes StartupEvent ev) {
    // log.info("1. CREATE DamlApp Instance");
    // }

    // WebSocket 연결 상태를 확인하는 메소드
    public boolean isConnectionOpen() {
        return this.session != null && this.session.isOpen();
    }

    // WebSocket 설정을 생성하고 연결을 초기화하는 메소드
    private void createConfig() {
        // log.info("WebSocket 설정을 생성합니다.");
        try {
            closeCurrentSession();
            initializeWebSocketContainer();
            connectToServer();
        } catch (Exception e) {
            log.error("WebSocket 설정 생성 중 오류 발생", e);
        }
    }

    @PreDestroy
    public void onDestroy() {
        try {
            closeCurrentSession();
            shutdownScheduler();
        } catch (IOException e) {
            log.error("WebSocket 세션 종료 중 오류 발생", e);
        }
    }

    // 기존 세션을 닫고 필드를 초기화하는 메소드
    private void closeCurrentSession() throws IOException {
        if (this.session != null && this.session.isOpen()) {
            this.session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "서버 종료"));
            log.info("기존 WebSocket 세션을 닫습니다.");
        }
        this.session = null;
        this.container = null;
    }

    private void shutdownScheduler() {
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                // 스케줄러가 종료될 때까지 대기
                if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                    // 스케줄러가 정상적으로 종료되지 않았다면 강제 종료
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                // 현재 스레드가 대기 중 인터럽트 되었다면 스케줄러를 즉시 종료
                scheduler.shutdownNow();
                // 인터럽트 상태를 복원
                Thread.currentThread().interrupt();
            }
            log.info("스케줄러가 종료되었습니다.");
        }
    }

    // WebSocket 컨테이너를 초기화하는 메소드
    private void initializeWebSocketContainer() {
        this.container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator();
        configurator
                .beforeRequest(Collections.singletonMap("Sec-WebSocket-Protocol", Collections.singletonList(PROTOCOL)));
        this.config = ClientEndpointConfig.Builder.create()
                .configurator(configurator)
                .preferredSubprotocols(Collections.singletonList(PROTOCOL))
                .build();
    }

    // 서버에 연결하는 메소드
    private void connectToServer() throws Exception {
        this.container.connectToServer(DamlApp.class, config, java.net.URI.create(URI));
    }

    // 연결 상태를 주기적으로 확인하는 메소드
    private void checkConnection() {
        if (isConnectionOpen()) {
            // log.info("WebSocket 연결이 유지되고 있습니다.");
        } else {
            log.error("WebSocket 연결이 끊어졌습니다.");
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        log.info("WebSocket 클라이언트가 연결됐습니다.");
        sendMessageToServer();
    }

    // 서버로 메시지를 보내는 메소드
    private void sendMessageToServer() {
        try {
            Gson gson = new Gson();
            List<String> templateIds = Arrays.asList(
                    "Account:AssetHoldingAccount",
                    "Account:AssetHoldingAccountProposal",
                    "Account:AssetHoldingAccountCloseProposal",
                    "Account:Trade",
                    "Account:TransferPreApproval",
                    "Account:AssetInSwap",
                    "Account:AssetHoldingAccountRequest",
                    "Account:AirdropRequest",
                    "Asset:Asset",
                    "Asset:AssetTransfer");
            Map<String, Object> messageContent = new HashMap<>();
            messageContent.put("templateIds", templateIds);
            String jsonMessage = gson.toJson(messageContent);
            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    // // log.info(message);
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(message);
                        JsonNode events = root.path("events");

                        for (JsonNode eventNode : events) {
                            String eventType = eventNode.fieldNames().next(); // 첫 번째 키를 이벤트 유형으로 사용
                            JsonNode eventData = eventNode.path(eventType);

                            // log.info(mapper.writeValueAsString(eventData));
                            DamlContractEntry entry = DamlContractEntry.builder()
                                    .event(eventType)
                                    .contract_id(eventData.path("contractId").asText())
                                    .contract(eventData)
                                    .hash(calculateMD5(mapper.writeValueAsString(eventData)))
                                    .build();
                            camelContext.createFluentProducerTemplate()
                                .to("direct:cache-operator-daml-put")
                                .withBody(entry)
                                .request(); //  반환 타입 필요없을듯 [2024-04-19 12:08:05]
                            /*
                             * DAMLFetchResponse result;
        try {
            result = ledger.fetchContractById(camelContext.createFluentProducerTemplate(), 
                Fetch.builder()
                .contractId("004bd589fbfe020dc7df7d519aae2a5872d50b3abdf6e73b6fac4625b2698962b4ca01122028bce3496cef3c64e1e4b3078aa152c29aad11795a280fa3dbd0d1f8c89abdcb")
                .templateId("Asset:Asset")
                .build());
                log.info(calculateMD5(result.getOriginalData().toString()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
                             */
                            log.info("entry: " + entry.toJson());
                        }
                    } catch (Exception e) {
                        log.error("메시지 처리 중 오류 발생", e);
                    }
                }
            });

            session.getAsyncRemote().sendText(jsonMessage, result -> {
                if (result.isOK()) {
                    // log.info("메시지 전송 성공: " + jsonMessage);
                } else {
                    log.error("메시지 전송 실패", result.getException());
                }
            });
        } catch (Exception e) {
            log.error("메시지 전송 중 예외 발생", e);
        }
    }

    private String calculateMD5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        return HexFormat.of().formatHex(messageDigest);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket 연결 중 오류 발생", throwable);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info("WebSocket 연결이 끊어졌습니다. 이유: " + closeReason.getReasonPhrase());
        tryReconnect();
    }

    // 재연결 시도 메소드
    private void tryReconnect() {
        scheduler.schedule(() -> {
            if (this.session == null || !this.session.isOpen()) {
                try {
                    log.info("WebSocket 재연결을 시도합니다.");
                    createConfig();
                } catch (Exception e) {
                    log.error("WebSocket 재연결 시도 중 오류 발생", e);
                    tryReconnect();
                }
            }
        }, 5, TimeUnit.SECONDS);
    }
}