package com.yeoboya.guinGujik.config.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void getToken(){
    }
}