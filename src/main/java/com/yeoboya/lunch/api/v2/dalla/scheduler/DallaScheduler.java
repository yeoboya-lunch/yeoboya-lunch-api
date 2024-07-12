package com.yeoboya.lunch.api.v2.dalla.scheduler;

import com.yeoboya.lunch.api.v2.dalla.constants.RankSearch;
import com.yeoboya.lunch.api.v2.dalla.response.DallaResponse;
import com.yeoboya.lunch.api.v2.dalla.service.DallaService;
import com.yeoboya.lunch.api.v2.dalla.service.TownService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@EnableScheduling
@Component
@Slf4j
public class DallaScheduler {

    private final DallaService dallaService;
    private final TownService townService;

    public DallaScheduler(DallaService dallaService, TownService townService) {
        this.dallaService = dallaService;
        this.townService = townService;
    }

    //30분마다
//    @Scheduled(cron = "0/30 * * * * ?")
    public void heartEveryThirtyMinutes() {
        try {
            dallaService.heart();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //매일 오전 1시 10분
//    @Scheduled(cron = "0 10 1 ? * *")
    public void attendance() {
        DallaResponse attendance = dallaService.attendance();
        log.warn("{}", attendance);
    }

    //매주 일요일 오전 1시 15분
//    @Scheduled(cron = "0 15 1 ? * SUN")
    public void attendanceBonus() {
        DallaResponse attendance = dallaService.attendanceBonus();
        log.warn("{}", attendance);
    }

    //매일 오전 8시 5분
    //@Scheduled(cron = "0 5 8 * * ?")
    public void bjDaily() {
        LocalDate localDate = LocalDate.now();
        String yesterday = localDate.minusDays(1).toString();
        dallaService.fanBoardWrite(RankSearch.BJ_DAILY.getRankSlct(), RankSearch.BJ_DAILY.getRankType(), yesterday, RankSearch.BJ_DAILY.getMessage());
    }

    //월요일 오전 1시
    //@Scheduled(cron = "0 0 1 * * MON")
    public void bjWeek() {
        LocalDate localDate = LocalDate.now();
        String yesterday = localDate.minusDays(1).toString();
        dallaService.fanBoardWrite(RankSearch.BJ_WEEK.getRankSlct(), RankSearch.BJ_WEEK.getRankType(), yesterday, RankSearch.BJ_WEEK.getMessage());
    }

    //매일 오전 8시 10분
    //@Scheduled(cron = "0 10 8 * * ?")
    public void fanDaily() {
        LocalDate localDate = LocalDate.now();
        String yesterday = localDate.minusDays(1).toString();
        dallaService.fanBoardWrite(RankSearch.FAN_DAILY.getRankSlct(), RankSearch.FAN_DAILY.getRankType(), yesterday, RankSearch.FAN_DAILY.getMessage());
    }

    // 5시 5분, 13시 5분, 19시 5분에 실행될 코드 - 삼시세끼 당근먹기 (B0001)
    //@Scheduled(cron = "0 5 7,13,19 * * ?")
    public void checkTimeRanking() {
        System.out.println("Running task at 5:05AM, 13:05PM, and 19:05PM");
        townService.missionUpdate("A0001", "B0001");
        townService.mission("A0001", "B0001");
    }

    // 1시 10분, 11시 10분, 19시 10분에 실행될 코드 - 타임랭킹 확인하기 (A0005)
    //@Scheduled(cron = "0 10 1,11,19 * * ?")
    public void eatCarrotThreeMeals() {
        System.out.println("Running task at 1:10AM, 11:10AM, and 19:10PM");
        townService.missionUpdate("A0001", "A0005");
        townService.mission("A0001", "A0005");
    }

    // 새벽 5시에 실행될 코드
    // A0001 - 출석체크 (새벽)
    //@Scheduled(cron = "0 0 5 * * ?")
    public void runAt5AM() {
        System.out.println("Running task at 5AM");
        townService.mission("A0001", "A0001");
    }

    // 밤 11시에 실행될 코드
    // A0003 - 시청하기 (저녁)
    // B0002 - 좋아요 (저녁)
    //@Scheduled(cron = "0 0 23 * * ?")
    public void runAt11PM() {
        System.out.println("Running task at 11PM");
        townService.mission("A0001", "A0003");
        townService.mission("A0001", "B0002");
    }
}
