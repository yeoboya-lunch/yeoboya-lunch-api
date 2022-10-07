package com.yeoboya.guinGujik.api._sample.controller;

import com.yeoboya.guinGujik.api._sample.service.SampleService;
import com.yeoboya.guinGujik.config.annotation.TimeLogging;
import com.yeoboya.guinGujik.config.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sample")
public class SampleController {

    private final SampleService sampleService;
    private final Response response;

    public SampleController(SampleService sampleService, Response response) {
        this.sampleService = sampleService;
        this.response = response;
    }

    @TimeLogging
    @GetMapping("/time-log")
    public ResponseEntity<?> sampleAction(){
        Map<String, Object> responseData = sampleService.sampleBiz();
        return response.success(responseData, "타임로깅");
    }

    @GetMapping("/time-no-log")
    public ResponseEntity<?> noLogging(){
        log.warn("no logging");
        return response.success("타임로깅 안찍힙니다.");

    }



}
