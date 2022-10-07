package com.yeoboya.guinGujik.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationEntryPoint
 * <p>
 * 인증 과정에서 실패하거나 인증을 위한 헤더정보를 보내지 않은 경우 401(UnAuthorized) 에러가 발생하게 된다.
 * <p>
 * Spring Security 에서 인증되지 않은 사용자에 대한 접근 처리는 AuthenticationEntryPoint 가 담당하는데 commence 메소드가 실행되어 처리된다.
 */

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        log.warn("fixme");
    }
}