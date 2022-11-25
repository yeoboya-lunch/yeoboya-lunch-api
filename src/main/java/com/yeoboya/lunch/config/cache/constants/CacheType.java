package com.yeoboya.lunch.config.cache.constants;

import lombok.Getter;

@Getter
public enum CacheType {

    CACHE_SHOP("cacheShop", 60, 10000),
    CACHE_ITEM("cacheItem", 10, 10000),
    RANDOM("random", 60, 10000);

    CacheType(String cacheName, int expiredAfterWrite, int maximumSize) {
        this.cacheName = cacheName;
        this.expiredAfterWrite = expiredAfterWrite;
        this.maximumSize = maximumSize;
    }

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;

}