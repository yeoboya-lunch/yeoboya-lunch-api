package com.yeoboya.lunch.api.v2.dalla.constants;

import lombok.Getter;

@Getter
public enum RankSearch {

    //BJ=1 / FAN=2 / CUPID=3
    //1=일간, 2=주간, 3=월간, 4=연간

    BJ_DAILY("1", "1", "BJ 일간랭킹 "),
    BJ_WEEK("1", "2", "BJ 주간랭킹 "),
    BJ_MONTH("1", "3", "BJ 월간랭킹 "),
    BJ_YEAR("1", "4", "BJ 연간랭킹 "),

    FAN_DAILY("2", "1","FAN 일간랭킹 "),
    FAN_WEEK("2", "2", "FAN 주간랭킹 "),
    FAN_MONTH("2", "3", "FAN 월간랭킹 ");

    private final String rankSlct;
    private final String rankType;
    private final String message;


    RankSearch(String s, String s1, String s2) {
        this.rankSlct = s;
        this.rankType = s1;
        this.message = s2;
    }
}
