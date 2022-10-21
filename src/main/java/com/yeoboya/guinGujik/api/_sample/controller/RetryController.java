package com.yeoboya.guinGujik.api._sample.controller;

import com.yeoboya.guinGujik.api.sms.dto.SmsRequestDto;
import com.yeoboya.guinGujik.api.sms.service.SmsService;
import com.yeoboya.guinGujik.config.annotation.TimeLogging;
import com.yeoboya.guinGujik.config.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sample/retry")
public class RetryController {

    private final SmsService smsService;
    private final Response response;

    public RetryController(SmsService smsService, Response response) {
        this.smsService = smsService;
        this.response = response;
    }

    @TimeLogging
    @GetMapping("/send-sms")
    public ResponseEntity<?> sendSms() {
        SmsRequestDto dto = SmsRequestDto.builder().
                rcvPhone("01083490706").
                rcvMemId("테스트").
                msgBody("[내용] 테스트 내용 입니다.").
                build();
        int result = smsService.sendSMS(dto);
        return response.success(result, "성공");
    }

}
