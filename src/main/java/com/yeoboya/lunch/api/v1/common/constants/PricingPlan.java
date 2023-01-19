package com.yeoboya.lunch.api.v1.common.constants;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

public enum PricingPlan {

    //1시간에 3번 사용가능한 무제한 요금제
    FREE {
        public Bandwidth getLimit() {
            return Bandwidth.classic(3, Refill.intervally(3, Duration.ofHours(1)));
        }
    },
    //1시간에 5 사용가능한 Basic 요금제
    BASIC {
        public Bandwidth getLimit() {
            return Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1)));
        }
    },
    //1시간에 10번 사용가능한 Professional 요금제
    PROFESSIONAL {
        public Bandwidth getLimit() {
            return Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1)));
        }
    };

    public abstract Bandwidth getLimit();

    public static PricingPlan resolvePlanFromApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return FREE;
        } else if (apiKey.startsWith("BA-")) {
            return BASIC;
        } else if (apiKey.startsWith("PX-")) {
            return PROFESSIONAL;
        }
        return FREE;
    }

}