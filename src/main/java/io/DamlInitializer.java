package io;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.camel.util.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.json.JsonArray;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.SendHandler;
import jakarta.websocket.SendResult;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

@ClientEndpoint
@ApplicationScoped
public class DamlInitializer extends Endpoint {

    private Session session;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private WebSocketContainer container;
    private ClientEndpointConfig config;

    static final Logger LOG = LoggerFactory.getLogger(DamlInitializer.class);
    private static final String URI = "ws://ec2-15-165-195-113.ap-northeast-2.compute.amazonaws.com:7575/v1/stream/query";
    private static final String PROTOCOL = "jwt.token.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsInNjb3BlIjoiZGFtbF9sZWRnZXJfYXBpIn0.HtipPGij-drbM0xLsfg5jeIMDhq1zejGsxWeq-FxkKo,daml.ws.auth";

    // / WebSocket 연결 상태를 확인하는 메소드
    public boolean isConnectionOpen() {
        return this.session != null && this.session.isOpen();
    }

    private void createConfig() {
        LOG.info("daml createConfig!!");
        try {
            this.container = ContainerProvider.getWebSocketContainer();
            ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator();
            configurator.beforeRequest(
                    Collections.singletonMap("Sec-WebSocket-Protocol", Collections.singletonList(PROTOCOL)));

            this.config = ClientEndpointConfig.Builder.create()
                    .configurator(configurator)
                    .preferredSubprotocols(Collections.singletonList(PROTOCOL))
                    .build();

            this.container.connectToServer(DamlInitializer.class, config, java.net.URI.create(URI));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStart(@Observes StartupEvent ev) {
        LOG.info("DamlInitializer!!");
        createConfig();

        scheduler.scheduleAtFixedRate(() -> {
            if (isConnectionOpen()) {
                // LOG.info("WebSocket 연결이 유지되고 있습니다.");
            } else {
                // LOG.error("WebSocket 연결이 끊어졌습니다.");
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    // @Override
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        LOG.info("1daml websocket client open!!");
        // 서버로 메시지를 보냅니다.
        try {
            JsonObject message = new JsonObject();
            JsonArray templateIdsArray = new JsonArray();
            String[] templateIds = {
                    "Account:AssetHoldingAccount",
                    "Account:AssetHoldingAccountProposal",
                    "Account:AssetHoldingAccountCloseProposal",
                    "Account:Trade",
                    "Account:TransferPreApproval",
                    "Account:AssetInSwap",
                    "Account:AssetHoldingAccountRequest",
                    "Account:AirdropRequest",
                    "Asset:Asset",
                    "Asset:AssetTransfer"
            };
            for (String templateId : templateIds) {
                templateIdsArray.add(templateId);
            }
            message.put("templateIds", templateIdsArray);
            LOG.info(message.toString());
            session.getBasicRemote().sendText(message.toString());
            // session.getAsyncRemote().sendText(message.toString());
            // , new SendHandler() {
            // @Override
            // public void onResult(SendResult result) {
            // if (result.isOK()) {
            // LOG.info("메시지 전송 성공");
            // } else {
            // LOG.error("메시지 전송 실패", result.getException());
            // }
            // }
            // });
        } catch (Exception e) {
            LOG.error("메시지 전송 중 예외 발생", e);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("Received message: " + message);
        // 메시지 처리 로직
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOG.error("WebSocket 연결 중 오류 발생", throwable);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        // LOG.info("WebSocket 연결이 끊어졌습니다. 이유: " + closeReason.getReasonPhrase());
        // 연결이 끊어진 후 재연결을 시도합니다.
        tryReconnect();
    }

    private void tryReconnect() {
        scheduler.schedule(() -> {
            try {
                LOG.info("WebSocket 재연결...");
                // 재연결 로직
                createConfig();
            } catch (Exception e) {
                LOG.error("WebSocket 재연결 시도 중 오류 발생", e);
                // 재연결 실패 시, 다시 재연결을 시도할 수 있습니다.
                tryReconnect();
            }
        }, 5, TimeUnit.SECONDS);
    }
}