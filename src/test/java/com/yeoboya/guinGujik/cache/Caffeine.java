package com.yeoboya.guinGujik.cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@Slf4j
@SpringBootTest
public class Caffeine {

    @Autowired
    CacheManager cacheManager;

    @Test
    @DisplayName("모든 로컬 캐시 조회")
    public void getAllCaches() {
        cacheManager.getCacheNames().forEach(log::info);
    }

}
