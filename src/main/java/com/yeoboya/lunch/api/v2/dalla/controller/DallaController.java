package com.yeoboya.lunch.api.v2.dalla.controller;

import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v2.dalla.constants.RankSearch;
import com.yeoboya.lunch.api.v2.dalla.response.DallaResponse;
import com.yeoboya.lunch.api.v2.dalla.service.DallaService;
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
    private final Response response;

    @PostMapping("/heart")
    public ResponseEntity<Response.Body> heart() {
        dallaService.heart();
        return response.success(Code.SEARCH_SUCCESS);
    }

    @PostMapping("/attendance")
    public ResponseEntity<Response.Body> attendance() {
        DallaResponse attendance = dallaService.attendance();
        return response.success(Code.SEARCH_SUCCESS, attendance);
    }

    @PostMapping("/fan-board-write")
    public ResponseEntity<Response.Body> fanBoardWrite() {
        LocalDate localDate = LocalDate.now();
        String yesterday = localDate.minusDays(1).toString();
        dallaService.fanBoardWrite(RankSearch.BJ_DAILY.getRankSlct(), RankSearch.BJ_DAILY.getRankType(), yesterday, RankSearch.BJ_DAILY.getMessage());
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
}
