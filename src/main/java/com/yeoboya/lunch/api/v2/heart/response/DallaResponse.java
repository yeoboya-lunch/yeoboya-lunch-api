package com.yeoboya.lunch.api.v2.heart.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DallaResponse {
    private String result;
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RoomList data;
}
