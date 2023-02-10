package com.yeoboya.lunch.api.v2.dalla.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@ToString
public class Data {
    private int totalCnt;
    private ArrayList<Response> list;

    @Getter
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response{
        private String rank;
        private String nickNm;
        private String memNo;
        private String roomNo;
        private String bjMemNo;
        private String clipNo;
    }
}

