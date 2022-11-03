package com.yeoboya.lunch.api.v1._sample.controller;


import com.yeoboya.lunch.config.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Tag(name = "다국어", description = "다국어 예제")
@Slf4j
@RestController
@RequestMapping("/v1/message")
public class MessageController {

    private final MessageSource messageSource;
    private final Response response;

    public MessageController(MessageSource messageSource, Response response) {
        this.messageSource = messageSource;
        this.response = response;
    }

    @Operation(summary = "다국어 테스트 메서드", description = "파파고 메서드입니다.")
    @GetMapping
    public ResponseEntity<?> papago(Locale locale){
        String welcome = messageSource.getMessage("welcome", null, locale);
        return response.success(welcome, "파파고 출동");
    }

}
