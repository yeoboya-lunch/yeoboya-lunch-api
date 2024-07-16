package com.yeoboya.lunch.api.v2.dalla.service;

import com.yeoboya.lunch.api.v2.dalla.websocket.WebSocketClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

@Service
public class WebSocketService {

    public void sendChatMessage(WebSocketClient webSocketClient, String memNo, String roomNo, String message) {
        if (roomNo == null || roomNo.isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be null or empty");
        }

        JSONObject chatObject = new JSONObject();
        chatObject.put("memNo", memNo);

        JSONObject dataInnerObject = new JSONObject();
        dataInnerObject.put("cmd", "chat");
        dataInnerObject.put("chat", chatObject);
        dataInnerObject.put("sendMsg", message);

        JSONObject dataObject = new JSONObject();
        dataObject.put("channel", roomNo);
        dataObject.put("data", dataInnerObject);

        JSONObject mainObject = new JSONObject();
        mainObject.put("event", "#publish");
        mainObject.put("data", dataObject);
        mainObject.put("cid", 4);

        Session session = webSocketClient.getUserSession();

        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(mainObject.toString());
        } else {
            System.out.println("Can't send message - WebSocket session is not open");
        }
    }

}
