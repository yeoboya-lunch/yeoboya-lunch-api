package com.yeoboya.lunch.config.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@RestController
public class PrometheusDemoController {
    private final MeterRegistry meterRegistry;

    private Counter counter;

    @PostConstruct
    public void init() {
        counter = meterRegistry.counter("api.call.count");
    }

    @GetMapping("/sample/count")
    public void count() {
        counter.increment();
    }
}