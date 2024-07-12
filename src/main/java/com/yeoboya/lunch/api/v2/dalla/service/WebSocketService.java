package com.yeoboya.lunch.api.v2.dalla.service;

import com.yeoboya.lunch.api.v2.dalla.websocket.WebSocketClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

@Service
public class WebSocketService {

    private WebSocketClient webSocketClient;
    private Integer messageCounter = 0;

    public void connect(String authToken, String memNo, String roomNo, String locale) {
        this.webSocketClient = new WebSocketClient(authToken, memNo, roomNo, locale);
    }

    public void sendChatMessage(String roomNo, String message) {
        if (roomNo == null || roomNo.isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be null or empty");
        }

        String numberedMessage = messageCounter + ": " + message;
        messageCounter++;

        JSONObject data = new JSONObject();
        data.put("event", "#publish");
        data.put("cid", 4);

        JSONObject innerData = new JSONObject();
        innerData.put("channel", roomNo);

        JSONObject details = new JSONObject();
        details.put("cmd", "chat");
        details.put("sendMsg", numberedMessage);

        innerData.put("data", details);
        data.put("data", innerData);

        Session session = webSocketClient.getUserSession();

        if (session != null && session.isOpen()) {
            webSocketClient.onMessage(data.toString());
        } else {
            System.out.println("Can't send message - WebSocket session is not open");
        }
    }

}
