package com.yeoboya.lunch.api.v2.dalla.controller;

import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v2.dalla.constants.RankSearch;
import com.yeoboya.lunch.api.v2.dalla.response.DallaResponse;
import com.yeoboya.lunch.api.v2.dalla.service.DallaService;
import com.yeoboya.lunch.api.v2.dalla.service.SocketService;
import com.yeoboya.lunch.api.v2.dalla.service.TownService;
import com.yeoboya.lunch.config.annotation.TimeLogging;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dalla")
public class DallaController {

    private final DallaService dallaService;
    private final TownService townService;
    private final SocketService socketService;
    private final Response response;

    @PostMapping("/heart")
    @TimeLogging
    public ResponseEntity<Response.Body> heart() {
        try {
            dallaService.heart();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.success(Code.SEARCH_SUCCESS);
    }

    @PostMapping("/attendance")
    public ResponseEntity<Response.Body> attendance() {
        DallaResponse attendance = dallaService.attendance();
        return response.success(Code.SEARCH_SUCCESS, attendance);
    }

    @PostMapping("/roulette")
    public ResponseEntity<Response.Body> roulette() {
        int roulette = dallaService.roulette();
        return response.success(Code.SEARCH_SUCCESS, roulette);
    }

    @PostMapping("/fan-board-write")
    public ResponseEntity<Response.Body> fanBoardWrite() {
        LocalDate localDate = LocalDate.now();
        String yesterday = localDate.minusDays(1).toString();
        dallaService.fanBoardWrite(RankSearch.FAN_DAILY.getRankSlct(), RankSearch.FAN_DAILY.getRankType(), yesterday, RankSearch.FAN_DAILY.getMessage());
        return response.success(Code.SEARCH_SUCCESS);
    }

    @PostMapping("/following-board-write")
    public ResponseEntity<Response.Body> followingBoardWrite() {
        dallaService.followingBoardWrite();
        return response.success(Code.SEARCH_SUCCESS);
    }


    @PostMapping("/add-fan")
    public ResponseEntity<Response.Body> addFan() {
        dallaService.fanAction(RankSearch.FAN_DAILY.getRankSlct(), RankSearch.FAN_DAILY.getRankType(), "2023-02-08");
        return response.success(Code.SAVE_SUCCESS);
    }

    @PostMapping("/clip/heart")
    public ResponseEntity<Response.Body> clipHeart(){
        dallaService.clipHeartAction();
        return response.success(Code.SEARCH_SUCCESS);
    }


    // A0001 - 출석체크 (새벽)
    // A0003 - 시청하기 (저녁)
    // A0005 - 타임랭킹 확인하기 (00:00 ~ 02:00 / 10:00 ~ 12:00 / 19:00 ~ 21:00)
    // B0001 - 삼시세끼 당근먹기 (07:00 ~ 11:00 / 12:00 ~ 15:00 / 18:00 ~ 21:00)
    // B0002 - 좋아요 (저녁)
    @PostMapping("/town/mission")
    public ResponseEntity<Response.Body> mission(){
        //부스터
        townService.missionUpdate("A0001", "A0006");
        townService.mission("A0001", "A0006");

        //부스터
        townService.missionUpdate("A0001", "A0006");
        townService.mission("A0001", "A0006");

        //삼시세끼 당근먹기
        townService.missionUpdate("A0001", "B0001");
        townService.mission("A0001", "B0001");

        //타임랭킹 확인하기
        townService.missionUpdate("A0001", "A0005");
        townService.mission("A0001", "A0005");

        //출석체크
        townService.missionUpdate("A0001", "A0001");
        townService.mission("A0001", "A0001");

        //시청하기
        townService.mission("A0001", "A0003");

        //좋아요
        townService.mission("A0001", "B0002");
        return response.success(Code.SEARCH_SUCCESS);
    }

    // A0006 fixme - 부스터 test
    // A0005 - 타임랭킹 확인하기 (00:00 ~ 02:00 / 10:00 ~ 12:00 / 19:00 ~ 21:00)
    // B0001 - 삼시세끼 당근먹기 (07:00 ~ 11:00 / 12:00 ~ 15:00 / 18:00 ~ 21:00)
    @PostMapping("/town/missionUpdate")
    public ResponseEntity<Response.Body> missionUpdate(){
        townService.missionUpdate("A0001", "A0006");
        return response.success(Code.SEARCH_SUCCESS);
    }

    @PostMapping("/socket")
    public ResponseEntity<Response.Body> socket(){
        socketService.sendUmmMessage();
        return response.success(Code.SEARCH_SUCCESS);
    }



}
