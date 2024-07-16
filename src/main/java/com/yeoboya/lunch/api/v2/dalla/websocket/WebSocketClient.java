package com.yeoboya.lunch.api.v2.dalla.websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.*;

import lombok.Getter;
import org.json.JSONObject;

@ClientEndpoint
public class WebSocketClient extends Endpoint {

    @Getter
    private Session userSession = null;
    private CountDownLatch latch = new CountDownLatch(1);

    public WebSocketClient(String socketUri, String authToken, String memNo, String roomNo) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            JSONObject data = new JSONObject();
            data.put("authToken", authToken);
            data.put("memNo", memNo);
            data.put("locale", "koKR");
            data.put("roomNo", roomNo);

            System.out.println(data);

            Map<String, String> headers = new HashMap<>();
            // Add your custom headers
            headers.put("os", "Your os");
            headers.put("deviceId", "Your deviceUuid");
            headers.put("deviceToken", "Your deviceToken");

            ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
                @Override
                public void beforeRequest(Map<String, List<String>> headersToServer) {
                    super.beforeRequest(headersToServer);
                    headers.forEach((key, value) -> headersToServer.put(key, List.of(value)));
                }
            };

            ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create().configurator(configurator).build();

            URI uri = new URI(socketUri + URLEncoder.encode(data.toString(), StandardCharsets.UTF_8));
            container.connectToServer(this, clientConfig, uri);

        } catch (IOException | URISyntaxException | DeploymentException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendHandshake() {
        JSONObject handshake = new JSONObject();
        handshake.put("event", "#handshake");
        handshake.put("data", new JSONObject());
        handshake.getJSONObject("data").put("authToken", JSONObject.NULL);
        handshake.put("cid", 1);
        onMessage(handshake.toString());
    }

    public void subscribePublic() {
        JSONObject subscription = new JSONObject();
        subscription.put("event", "#subscribe");
        JSONObject subscriptionData = new JSONObject();
        subscriptionData.put("channel", "channel.public.dalbit");
        subscription.put("data", subscriptionData);
        subscription.put("cid", 2);
        onMessage(subscription.toString());
    }


    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config) {
        System.out.println("Connected");
        this.userSession = userSession;
        latch.countDown();
        sendHandshake();
        subscribePublic();
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("Disconnected");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received: " + message);
        this.userSession.getAsyncRemote().sendText(message);
    }

    public boolean awaitConnection(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);  // Wait until the latch has counted down to zero
    }
}
