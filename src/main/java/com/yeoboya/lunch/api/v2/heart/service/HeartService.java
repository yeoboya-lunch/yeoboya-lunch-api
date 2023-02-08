package com.yeoboya.lunch.api.v2.heart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v2.heart.response.DallaResponse;
import com.yeoboya.lunch.api.v2.heart.response.RoomList;
import com.yeoboya.lunch.config.util.OkhttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartService {

    private final OkhttpClient client;
    private final ObjectMapper objectMapper;

    public void action() {
        List<RoomList.Room> rooms = this.roomList();
        Collections.reverse(rooms);
        int cnt = 0;
//        rooms.forEach(room -> this.joinRoomAndHeart(room.getRoomNo(), room.getBjMemNo()));
        for (RoomList.Room room : rooms) {
            DallaResponse joinRoom = this.joinRoom(room.getRoomNo());
            if (joinRoom.getResult().equals("success")) {
                DallaResponse heart = this.heart(room.getRoomNo(), room.getBjMemNo());
                if (heart.getResult().equals("success")) {
                    cnt++;
                }
            }
        }
        log.warn("{}/{}", cnt, rooms.size());
    }

    public List<RoomList.Room> roomList() {
        Map<String, String> params = new HashMap<>();
        params.put("memNo", "1");
        params.put("searchData", "");
        params.put("mediaType", "t");
        params.put("pageNo", "1");
        params.put("pagePerCnt", "10000");
        String s = client.sendGet("/broad/roomlist", params);
        try {
            DallaResponse dallaResponse = objectMapper.readValue(s, DallaResponse.class);
            return dallaResponse.getData().getList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public DallaResponse joinRoom(String roomNo) {
        RequestBody body = new FormBody.Builder()
                .add("roomNo", roomNo).build();
        String s = client.sendPost("/broad/vw/join", body);
        try {
            return objectMapper.readValue(s, DallaResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public DallaResponse heart(String roomNo, String bjMemNo) {
        RequestBody body = new FormBody.Builder()
                .add("roomNo", roomNo)
                .add("bjMemNo", bjMemNo)
                .build();
        String s = client.sendPost("/broad/likes", body);
        try {
            return objectMapper.readValue(s, DallaResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinRoomAndHeart(String roomNo, String bjMemNo) {
        DallaResponse joinRoom = this.joinRoom(roomNo);
        if(joinRoom.getResult().equals("success")){
            DallaResponse heart = this.heart(roomNo, bjMemNo);
            log.warn("{}", heart);
        }
    }

}


