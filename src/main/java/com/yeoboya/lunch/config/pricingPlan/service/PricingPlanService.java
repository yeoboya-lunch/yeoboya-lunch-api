package com.yeoboya.lunch.config.pricingPlan.service;

import com.yeoboya.lunch.config.pricingPlan.constants.PricingPlan;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PricingPlanService {

    // api키를 키로, Bucket 객체를 값으로 가지는 캐시를 생성합니다. ConcurrentHashMap을 사용하여 스레드 안전성을 보장합니다.
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // api 키를 인자로 받아 해당 api 키에 해당하는 Bucket을 반환합니다. 없을 경우 새 Bucket을 생성합니다.
    public Bucket resolveBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    // api 키를 인자로 받아 newBucket() 메소드를 통해 새로운 Bucket을 생성합니다.
    // 생성된 Bucket은 api 키가 속한 요금제(PricingPlan)의 한계값을 가지는 limit를 설정합니다.
    private Bucket newBucket(String apiKey) {
        PricingPlan pricingPlan = PricingPlan.resolvePlanFromApiKey(apiKey);
        return Bucket.builder()
                .addLimit(pricingPlan.getLimit())
                .build();
    }
}
