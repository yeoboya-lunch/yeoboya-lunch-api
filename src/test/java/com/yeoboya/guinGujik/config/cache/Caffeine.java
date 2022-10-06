package com.yeoboya.guinGujik.config.cache;

import com.yeoboya.guinGujik.config.constants.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @AllArgsConstructor
    @Getter
    @Setter
    public class TestVO{

        private String email;
        private String password;

        private List<String> roles = new ArrayList<>();


    }

    @Test
    public void addList(){
        TestVO testVO = new TestVO("asd", "123", Collections.singletonList(Authority.ROLE_USER.name()));

        testVO.getRoles().add("123");
        log.warn("{}", testVO);
    }
}
