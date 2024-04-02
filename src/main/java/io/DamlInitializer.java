package io;

import java.io.IOException;
import java.util.Collections;

import org.apache.camel.util.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

@ClientEndpoint
@ApplicationScoped
public class DamlInitializer extends Endpoint {

    static final Logger LOG = LoggerFactory.getLogger(DamlInitializer.class);
    private static final String URI = "ws://ec2-15-165-195-113.ap-northeast-2.compute.amazonaws.com:7575/v1/stream/query";
    private static final String PROTOCOL = "jwt.token.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwczovL2RhbWwuY29tL2xlZGdlci1hcGkiOnsibGVkZ2VySWQiOiJzYW5kYm94IiwiYXBwbGljYXRpb25JZCI6IndhbGxldC1yZWZhcHAiLCJhY3RBcyI6WyJhZG1pbiJdfX0.51b2lpNUsQXBr08-GqUEx_X7P1OuG31ACmN3t5TyoRQ,daml.ws.auth";

    public void onStart(@Observes StartupEvent ev) {
        LOG.info("DamlInitializer!!");
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator();
            configurator.beforeRequest(
                    Collections.singletonMap("Sec-WebSocket-Protocol", Collections.singletonList(PROTOCOL)));

            ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                    .configurator(configurator)
                    .preferredSubprotocols(Collections.singletonList(PROTOCOL))
                    .build();

            container.connectToServer(DamlInitializer.class, config, java.net.URI.create(URI));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Override
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        LOG.info("daml websocket client open!!");
        // 서버로 메시지를 보냅니다.
        try {
            JsonObject message = new JsonObject();
            message.put("templateIds", new String[] {
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
            });
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    void message(String msg) {
        LOG.info(msg);
    }
}