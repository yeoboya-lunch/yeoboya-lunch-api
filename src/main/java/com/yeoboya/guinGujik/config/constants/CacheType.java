package com.yeoboya.guinGujik.config.constants;

import lombok.Getter;

@Getter
public enum CacheType {

    GREET("greet", 5 * 60, 10000),
    RANDOM("random", 60, 10000);

    CacheType(String cacheName, int expiredAfterWrite, int maximumSize) {
        this.cacheName = cacheName;
        this.expiredAfterWrite = expiredAfterWrite;
        this.maximumSize = maximumSize;
    }

    private String cacheName;
    private int expiredAfterWrite;
    private int maximumSize;

}