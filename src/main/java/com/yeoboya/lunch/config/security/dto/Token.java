package com.yeoboya.lunch.config.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Token {

    private String subject;
    private String id;
    private String issuer;
    private String issueDAt;
    private String accessToken;
    private String refreshToken;
    private String tokenExpirationTime;         //access token 유효기간
    private long refreshTokenExpirationTime;    //refresh token 유효기간
    private String refreshTokenExpirationTimeStr;    //refresh token 유효기간

}