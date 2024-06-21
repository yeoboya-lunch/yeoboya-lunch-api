package com.yeoboya.lunch.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.config.security.JwtTokenProvider;
import com.yeoboya.lunch.config.security.dto.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Token token = jwtTokenProvider.generateToken(authentication);
        String tokenJson = objectMapper.writeValueAsString(token);

        boolean memberExists = (boolean) request.getSession().getAttribute("memberExists");

        response.setContentType("application/json;charset=UTF-8");
        if (memberExists) {
            response.getWriter().write("{\"message\": \"이미 가입된 회원 입니다.\", \"token\": " + tokenJson + "}");
        } else {
            response.getWriter().write("{\"message\": \"회원 가입을 진행 해주세요.\", \"token\": " + token.getSubject() + "}");
        }
    }
}
