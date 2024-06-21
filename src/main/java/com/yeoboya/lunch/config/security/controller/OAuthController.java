package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.config.security.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/code/{registrationId}")
    public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        log.error("{}, {}", code, registrationId);
        System.out.println("code = " + code + ", registrationId = " + registrationId);
        oAuthService.socialLogin(code, registrationId);
    }
}
