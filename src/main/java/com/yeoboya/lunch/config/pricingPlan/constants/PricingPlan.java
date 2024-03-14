package com.yeoboya.lunch.config.pricingPlan.constants;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import java.time.Duration;

public enum PricingPlan {

    FREE {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(10, Refill.intervally( 10, Duration.ofMinutes(1)));
        }
    },

    BASIC {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(20, Refill.intervally(20, Duration.ofMinutes(1)));
        }
    },

    PROFESSIONAL {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(50, Refill.intervally(50, Duration.ofMinutes(1)));
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
