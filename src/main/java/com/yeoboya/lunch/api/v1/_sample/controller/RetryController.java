package com.yeoboya.lunch.api.v1._sample.controller;

import com.yeoboya.lunch.api.v1.sms.model.SmsRequestDto;
import com.yeoboya.lunch.api.v1.sms.service.SmsService;
import com.yeoboya.lunch.config.annotation.TimeLogging;
import com.yeoboya.lunch.config.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/retry")
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
