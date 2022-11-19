package com.yeoboya.lunch.api.v1.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Body {

    private int code;
    private String message;
    private Object data;
    private Object error;

}