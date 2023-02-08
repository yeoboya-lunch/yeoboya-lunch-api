package com.yeoboya.lunch.api.v2.dalla.scheduler;

import com.yeoboya.lunch.api.v2.dalla.response.DallaResponse;
import com.yeoboya.lunch.api.v2.dalla.service.DallaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@Slf4j
public class DallaScheduler {

    private final DallaService dallaService;

    public DallaScheduler(DallaService dallaService) {
        this.dallaService = dallaService;
    }

    @Scheduled(cron = "0 0/10 * 1/1 * ? *")
    public void heartEveryTenMinutes() {
        dallaService.heart();
    }

    @Scheduled(cron ="0 0 2 1/1 * ? *")
    public void attendanceCheck(){
        DallaResponse attendance = dallaService.attendance();
        log.warn("{}", attendance);
    }


}
