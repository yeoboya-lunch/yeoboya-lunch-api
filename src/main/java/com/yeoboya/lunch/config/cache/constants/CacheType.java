package com.yeoboya.lunch.config.cache.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {

    RANDOM("random", 60, 10000),
    RESOURCE("resourceList", 60, 10000),
    ACCESS_IP("accessIpList", 60, 10000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;

}
