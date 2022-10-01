package com.yeoboya.guinGujik.api._sample.controller;

import com.yeoboya.guinGujik.annotation.TimeLogging;
import com.yeoboya.guinGujik.api._sample.service.SampleService;
import com.yeoboya.guinGujik.common.BasicResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @TimeLogging
    @GetMapping("/sample")
    public BasicResponse sampleAction(HttpServletRequest request, HttpServletResponse response){

        Map<String, Object> responseData = sampleService.sampleBiz();

        BasicResponse basicResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("경험치 조회 성공~ㅋ")
                .result(responseData)
                .build();

        return basicResponse;
    }

    @GetMapping("/time-no-logging")
    public BasicResponse noLogging(HttpServletRequest request, HttpServletResponse response){
        log.warn("no logging");

        BasicResponse basicResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("TimeLogging 어노테이션 없으면 노깅 안찍혀요~")
                .result(null)
                .build();

        return basicResponse;
    }



}
