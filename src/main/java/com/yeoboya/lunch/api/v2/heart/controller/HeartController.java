package com.yeoboya.lunch.api.v2.heart.controller;

import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v2.heart.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dalla")
public class HeartController {

    private final HeartService heartService;
    private final Response response;

    @PostMapping("/heart")
    public ResponseEntity<Response.Body> action(){
        heartService.action();
        return response.success(Code.SEARCH_SUCCESS);

    }
}
