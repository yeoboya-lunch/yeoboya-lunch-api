package com.yeoboya.guinGujik.config.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String subject;
        private String id;
        private String issuer;
        private String issueDAt;
        private String accessToken;
        private String tokenExpirationTime;           //토큰유효기간
        private String refreshToken;
        private String refreshTokenExpirationTime;    //refresh 토큰유효기간
        private String grantType;
    }

}