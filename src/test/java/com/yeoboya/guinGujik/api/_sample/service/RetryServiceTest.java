package com.yeoboya.guinGujik.api._sample.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
class RetryServiceTest {

    @Autowired
    RetryService retryService;

    @Test
    void retryTest() {
        for (int i = 0; i < 5; i++) {
            log.info("client request={}", i);
            retryService.read("data"+ i);
        }
    }
}