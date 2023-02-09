package com.yeoboya.lunch.api.v2.dalla.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v2.dalla.response.DallaResponse;
import com.yeoboya.lunch.api.v2.dalla.response.Data;
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
public class DallaService {

    private final OkhttpClient client;
    private final ObjectMapper objectMapper;

    public void heart() {
        List<Data.Response> rooms = this.roomList();
        Collections.reverse(rooms);
        int cnt = 0;
        for (Data.Response room : rooms) {
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

    public List<Data.Response> roomList() {
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
        if (joinRoom.getResult().equals("success")) {
            DallaResponse heart = this.heart(roomNo, bjMemNo);
            log.warn("{}", heart);
        }
    }

    public DallaResponse attendance() {
        String s = client.sendPost("/event/attendance/check/in");
        try {
            return objectMapper.readValue(s, DallaResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void fanBoardWrite(String rankSlct, String rankType, String rankingDate, String message) {
        List<Data.Response> ranks = this.rankList(rankSlct, rankType, rankingDate);
        int cnt = 0;
        for (Data.Response rank : ranks) {
            String contents = "안녕하세요~🙇‍ " + rank.getNickNm() + "님" +
                    "\n[" + rankingDate + "] " + message + rank.getRank() + "위 축하드립니다.🥳";
            DallaResponse write = this.write(rank.getMemNo(), contents);
            if (write.getResult().equals("success")) {
                cnt++;
            }
        }
        log.warn("{}/{}", cnt, ranks.size());
    }


    //랭킹 정보 가져오기
    public List<Data.Response> rankList(String rankSlct, String rankType, String rankingDate) {
        Map<String, String> params = new HashMap<>();
        params.put("rankSlct", rankSlct);
        params.put("rankType", rankType);
        params.put("rankingDate", rankingDate);
        params.put("pageNo", "1");
        params.put("records", "1");
        String s = client.sendGet("/rank/list", params);
        try {
            DallaResponse dallaResponse = objectMapper.readValue(s, DallaResponse.class);
            return dallaResponse.getData().getList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //방명록 쓰기
    public DallaResponse write(String memNo, String contents) {
        RequestBody body = new FormBody.Builder()
                .add("memNo", memNo)
                .add("depth", "1")
                .add("contents", contents)
                .add("viewOn", "1")
                .build();
        String s = client.sendPost("/profile/board", body);
        try {
            return objectMapper.readValue(s, DallaResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}


