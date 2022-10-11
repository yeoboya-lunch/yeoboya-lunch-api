package com.yeoboya.guinGujik.config.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Token {

    //todo: subject, id, issuer, issueDAt, tokenExpirationTime, refreshTokenExpirationTime return 할지 고민 필요.
    private String subject;
    private String id;
    private String issuer;
    private String issueDAt;
    private String accessToken;
    private String tokenExpirationTime;           //토큰유효기간
    private String refreshToken;
    private String refreshTokenExpirationTime;    //refresh 토큰유효기간

    @Getter
    @Builder
    @AllArgsConstructor
    public static class IssuedToken {
        private String subject;
        private String id;
        private String issuer;
        private String issueDAt;
        private String accessToken;
        private String tokenExpirationTime;           //토큰유효기간
        private String auth;

        public IssuedToken() {}

        public static IssuedToken getToken(String subject, String id, String issuer, String issueDAt, String accessToken, String tokenExpirationTime, String auth) {
            IssuedToken token = new IssuedToken();
            token.subject = subject;
            token.id = id;
            token.issuer = issuer;
            token.issueDAt = issueDAt;
            token.accessToken = accessToken;
            token.tokenExpirationTime = tokenExpirationTime;
            token.auth = auth;
            return token;
        }
    }

}