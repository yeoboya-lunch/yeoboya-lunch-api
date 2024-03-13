package com.yeoboya.lunch.api.v1.member.domain;

import com.yeoboya.lunch.config.security.reqeust.ClientRequestInfo;
import com.yeoboya.lunch.config.util.IPUtils;
import lombok.*;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class LoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remoteIp;
    private String sessionId;
    private LocalDateTime loginTime;
    private String userAgent;
    private Locale locale;
    private String requestUri;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public static LoginInfo buildLoginInfo(Member member, HttpServletRequest request) {
        return LoginInfo.builder()
                .member(member)
                .remoteIp(IPUtils.getClientIP(request))
                .sessionId(request.getSession().getId())
                .userAgent(request.getHeader("User-Agent"))
                .locale(request.getLocale())
                .loginTime(LocalDateTime.now())
                .requestUri(request.getRequestURI())
                .build();
    }
}
