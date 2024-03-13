package com.yeoboya.lunch.config.pricingPlan.constants;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import java.time.Duration;

public enum PricingPlan {

    // 무제한 요금제: 1시간에 최대 3회 사용 가능
    FREE {
        @Override
        public Bandwidth getLimit() {
            // 1시간마다 3토큰을 채우는 제한 대역폭을 반환합니다.
            return Bandwidth.classic(3, Refill.intervally(3, Duration.ofHours(1)));
        }
    },

    // Basic 요금제: 1시간에 최대 5회 사용 가능
    BASIC {
        @Override
        public Bandwidth getLimit() {
            // 1시간마다 5토큰을 채우는 제한 대역폭을 반환합니다.
            return Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1)));
        }
    },

    // Professional 요금제: 1시간에 최대 10회 사용 가능
    PROFESSIONAL {
        @Override
        public Bandwidth getLimit() {
            // 1시간마다 10토큰을 채우는 제한 대역폭을 반환합니다.
            return Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1)));
        }
    };

    // 요금제에 따른 제한 대역폭을 반환하는 추상 메소드
    public abstract Bandwidth getLimit();

    // API 키를 활용하여 해당 요금제를 결정하는 메소드
    public static PricingPlan resolvePlanFromApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.startsWith("FR-")) {
            return FREE;
        } else if (apiKey.startsWith("BA-")) {
            return BASIC;
        } else if (apiKey.startsWith("PR-")) {
            return PROFESSIONAL;
        }
        return FREE;
    }
}
