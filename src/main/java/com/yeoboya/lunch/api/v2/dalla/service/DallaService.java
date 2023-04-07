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

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DallaService {

    private final OkhttpClient client;
    private final ObjectMapper objectMapper;

    public void heart() {
        List<Data.Response> rooms = this.roomList();
        Collections.reverse(rooms);

//        Random random = new Random();
//        int randomIndex = random.nextInt(rooms.size());
//        Data.Response x = rooms.get(randomIndex);
//        DallaResponse joinRoom = this.joinRoom(x.getRoomNo());
//        log.warn("{}", joinRoom);

        int cnt = 0;
        for (Data.Response room : rooms) {
            DallaResponse joinRoom = this.joinRoom(room.getRoomNo());
            if (joinRoom.getResult().equals("success")) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                DallaResponse heart = this.heart(room.getRoomNo(), room.getBjMemNo());
                if (heart.getResult().equals("success")) {
                    cnt++;
                }
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        log.warn("{}/{}", cnt, rooms.size());
    }

    //방송방 가져오기
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

    //방입장
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

    //방송방 좋아요 누르기
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

    //출석체크
    public DallaResponse attendance() {
        String s = client.sendPost("/event/attendance/check/in");
        try {
            return objectMapper.readValue(s, DallaResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //팬보드 글작성
    public void fanBoardWrite(String rankSlct, String rankType, String rankingDate, String message) {
        List<Data.Response> ranks = this.rankList(rankSlct, rankType, rankingDate);
        int cnt = 0;
        for (Data.Response rank : ranks) {
            String contents = "안녕하세요~🙇‍ " + rank.getNickNm() + "님" +
                    "\n[" + rankingDate + "] " + message + rank.getRank() + "위 축하드립니다.🥳" +
                    "\n 오늘도 멋진 방송 부탁해요 화이팅~~~🎙️";
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
        params.put("records", "150");
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



    public void fanAction(String rankSlct, String rankType, String rankingDate) {
        List<Data.Response> ranks = this.rankList(rankSlct, rankType, rankingDate);
        int cnt = 0;
        for (Data.Response rank : ranks) {
            DallaResponse fan = this.fan(rank.getMemNo());
            log.warn("{}", fan);
            if (fan.getResult().equals("success")) {
                cnt++;
            }
        }
        log.warn("{}/{}", cnt, ranks.size());
    }



    //팬등록
    public DallaResponse fan(String memNo){
        RequestBody body = new FormBody.Builder()
                .add("memNo", memNo)
                .build();
        String s = client.sendPost("/mypage/fan", body);
        try {
            return objectMapper.readValue(s, DallaResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void clipHeartAction(){
        List<Data.Response> clipList = this.clipList();
        int cnt = 0;
        for (Data.Response clip : clipList) {
            DallaResponse clipHeart = this.clipHeart(clip.getClipNo());
            log.warn("{}", clipHeart);
            if (clipHeart.getResult().equals("success")) {
                cnt++;
            }
        }
        log.warn("{}/{}", cnt, clipList.size());
    }

    //클립 리스트
    public List<Data.Response> clipList(){
        Map<String, String> params = new HashMap<>();
        params.put("gender", "");
        params.put("djType", "0");
        params.put("slctType", "6");
        params.put("dateType", "0");
        params.put("records", "7000");
        params.put("subjectType", "");
        String s = client.sendGet("/clip/list", params);
        try {
            DallaResponse dallaResponse = objectMapper.readValue(s, DallaResponse.class);
            return dallaResponse.getData().getList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    //클립 좋아요 누르기
    public DallaResponse clipHeart(String clipNo){
        RequestBody body = new FormBody.Builder()
                .add("clipNo", clipNo)
                .add("good", "1")
                .build();
        String s = client.sendPost("/clip/good", body);
        try {
            return objectMapper.readValue(s, DallaResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}


