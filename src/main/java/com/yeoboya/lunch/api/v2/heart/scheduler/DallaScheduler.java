package com.yeoboya.lunch.api.v2.heart.scheduler;

import com.yeoboya.lunch.api.v2.heart.service.HeartService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class DallaScheduler {

    private final HeartService heartService;

    public DallaScheduler(HeartService heartService) {
        this.heartService = heartService;
    }

    @Scheduled(cron = "0 0/10 * * * * ")
    public void fixedRateScheduler() {
        heartService.action();
    }


}
