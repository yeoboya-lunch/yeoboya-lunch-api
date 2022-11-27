package com.yeoboya.lunch.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 인증예외
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();

        if (e instanceof BadCredentialsException) {
            body.put("message", "아이디 또는 비밀번호를 잘못 입력했습니다.");
        } else if (e instanceof InternalAuthenticationServiceException) {
            body.put("message", "내부적으로 발생한 시스템 문제로 인해 인증 요청을 처리할 수없습니다.");
        } else if (e instanceof DisabledException) {
            body.put("message", "계정이 비활성화 상태입니다. 관리자에게 문의하세요.");
        } else if (e instanceof CredentialsExpiredException) {
            body.put("message", "자격 증명 유효 기간이 만료되었습니다.");
        } else {
            body.put("message", e.getMessage());
        }

        body.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("data", "엑세스 토큰이 없습니다.");

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(httpServletResponse.getOutputStream(), body);
    }
}