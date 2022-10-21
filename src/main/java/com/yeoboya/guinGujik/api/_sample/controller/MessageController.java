package com.yeoboya.guinGujik.api._sample.controller;


import com.yeoboya.guinGujik.config.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/sample/message")
public class MessageController {

    private final MessageSource messageSource;
    private final Response response;

    public MessageController(MessageSource messageSource, Response response) {
        this.messageSource = messageSource;
        this.response = response;
    }

    @GetMapping
    public ResponseEntity<?> papago(Locale locale){
        String welcome = messageSource.getMessage("welcome", null, locale);
        return response.success(welcome, "파파고 출동");
    }

}
