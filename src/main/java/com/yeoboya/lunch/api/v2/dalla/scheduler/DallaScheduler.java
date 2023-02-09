package com.yeoboya.lunch.api.v2.dalla.scheduler;

import com.yeoboya.lunch.api.v2.dalla.constants.RankSearch;
import com.yeoboya.lunch.api.v2.dalla.service.DallaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@EnableScheduling
@Component
@Slf4j
public class DallaScheduler {

    private final DallaService dallaService;

    public DallaScheduler(DallaService dallaService) {
        this.dallaService = dallaService;
    }

    // 10분마다
//    @Scheduled(cron = "0 0/10 * * * ?")
//    public void heartEveryTenMinutes() {
//        dallaService.heart();
//    }

    //매일 오전 3시 10분
//    @Scheduled(cron = "0 10 3 ? * *")
//    public void attendanceCheck() {
//        DallaResponse attendance = dallaService.attendance();
//        log.warn("{}", attendance);
//    }

    //매일 오전 8시 5분
    @Scheduled(cron = "0 5 8 * * ?")
    public void bjDaily() {
        LocalDate localDate = LocalDate.now();
        String yesterday = localDate.minusDays(1).toString();
        dallaService.fanBoardWrite(RankSearch.BJ_DAILY.getRankSlct(), RankSearch.BJ_DAILY.getRankType(), yesterday, RankSearch.BJ_DAILY.getMessage());
    }

    //월요일 오전 1시
//    @Scheduled(cron = "0 0 1 * * MON")
//    public void bjWeek() {
//        LocalDate localDate = LocalDate.now();
//        String yesterday = localDate.minusDays(1).toString();
//        dallaService.fanBoardWrite(RankSearch.BJ_WEEK.getRankSlct(), RankSearch.BJ_WEEK.getRankType(), yesterday, RankSearch.BJ_WEEK.getMessage());
//    }

    //매일 오전 8시 10분
    @Scheduled(cron = "0 10 8 * * ?")
    public void fanDaily() {
        LocalDate localDate = LocalDate.now();
        String yesterday = localDate.minusDays(1).toString();
        dallaService.fanBoardWrite(RankSearch.FAN_DAILY.getRankSlct(), RankSearch.FAN_DAILY.getRankType(), yesterday, RankSearch.FAN_DAILY.getMessage());
    }

}
