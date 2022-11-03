package com.yeoboya.lunch.api.v1._sample.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheService {

    @CacheEvict(value = "random", allEntries = true)
    public String removeAllCache() {
        log.warn("delete All cache");
        return "OK";
    }

    @CacheEvict(value = "random", key = "#id")
    public String removeCache(String id) {
        log.warn("delete {} cache", id);
        return "OK";
    }
}
