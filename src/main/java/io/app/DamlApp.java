package io.app;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
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

@ClientEndpoint
@ApplicationScoped
public class DamlApp extends Endpoint {

    private Session session;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private WebSocketContainer container;
    private ClientEndpointConfig config;

    static final Logger LOG = LoggerFactory.getLogger(DamlApp.class);
    @ConfigProperty(name = "daml.ws.uri")
    private String URI;

    @ConfigProperty(name = "daml.ws.protocol")
    private String PROTOCOL;


    public void onStart(@Observes StartupEvent ev) {
        LOG.info("DamlInitializer 시작됨.");
        createConfig();
        scheduler.scheduleAtFixedRate(this::checkConnection, 0, 3, TimeUnit.SECONDS);
    }

    // WebSocket 연결 상태를 확인하는 메소드
    public boolean isConnectionOpen() {
        return this.session != null && this.session.isOpen();
    }

    // WebSocket 설정을 생성하고 연결을 초기화하는 메소드
    private void createConfig() {
        // LOG.info("WebSocket 설정을 생성합니다.");
        try {
            closeCurrentSession();
            initializeWebSocketContainer();
            connectToServer();
        } catch (Exception e) {
            LOG.error("WebSocket 설정 생성 중 오류 발생", e);
        }
    }

    // 기존 세션을 닫고 필드를 초기화하는 메소드
    private void closeCurrentSession() throws IOException {
        if (this.session != null && this.session.isOpen()) {
            this.session.close();
            LOG.info("기존 WebSocket 세션을 닫습니다.");
        }
        this.session = null;
        this.container = null;
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
        // LOG.info("서버에 연결을 시도합니다.");
    }

    // 연결 상태를 주기적으로 확인하는 메소드
    private void checkConnection() {
        if (isConnectionOpen()) {
            // LOG.info("WebSocket 연결이 유지되고 있습니다.");
        } else {
            // LOG.error("WebSocket 연결이 끊어졌습니다.");
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        LOG.info("WebSocket 클라이언트가 연결됐습니다.");
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
                    LOG.info("OnMessage: " + message);
                }
            });

            session.getAsyncRemote().sendText(jsonMessage, result -> {
                if (result.isOK()) {
                    // LOG.info("메시지 전송 성공: " + jsonMessage);
                } else {
                    LOG.error("메시지 전송 실패", result.getException());
                }
            });
        } catch (Exception e) {
            LOG.error("메시지 전송 중 예외 발생", e);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOG.error("WebSocket 연결 중 오류 발생", throwable);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        LOG.info("WebSocket 연결이 끊어졌습니다. 이유: " + closeReason.getReasonPhrase());
        tryReconnect();
    }

    // 재연결 시도 메소드
    private void tryReconnect() {
        scheduler.schedule(() -> {
            if (this.session == null || !this.session.isOpen()) {
                try {
                    LOG.info("WebSocket 재연결을 시도합니다.");
                    createConfig();
                } catch (Exception e) {
                    LOG.error("WebSocket 재연결 시도 중 오류 발생", e);
                    tryReconnect();
                }
            }
        }, 5, TimeUnit.SECONDS);
    }
}