package com.yeoboya.lunch.api.v2.heart.response;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@ToString
public class RoomList {
    private int totalCnt;
    private ArrayList<Room> list;

    @Getter
    @ToString
    public static class Room{
        private String roomNo;
        private String bjMemNo;
    }
}

