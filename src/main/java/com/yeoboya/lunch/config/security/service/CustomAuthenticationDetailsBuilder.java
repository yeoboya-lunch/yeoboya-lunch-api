package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.config.security.reqeust.ClientRequestInfo;
import com.yeoboya.lunch.config.util.IPUtils;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationDetailsBuilder implements AuthenticationDetailsSource<HttpServletRequest, ClientRequestInfo> {

    @Override
    public ClientRequestInfo buildDetails(HttpServletRequest request) {
        return ClientRequestInfo.builder()
                .remoteIp(IPUtils.getClientIP(request))
                .sessionId(request.getSession().getId())
                .loginTime(LocalDateTime.now())
                .requestUri(request.getRequestURI())
                .build();
    }
}
