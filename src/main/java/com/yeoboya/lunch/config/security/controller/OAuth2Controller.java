package com.yeoboya.lunch.config.security.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@RequestMapping("/login/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {

    @GetMapping("")
    public String google(){
        return "http://localhost:8080/oauth2/authorization/google";
    }
}
