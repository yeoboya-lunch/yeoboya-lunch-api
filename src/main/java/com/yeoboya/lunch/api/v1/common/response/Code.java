package com.yeoboya.lunch.api.v1.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Getter
public enum Code {

    SEARCH_SUCCESS("조회", OK),
    SAVE_SUCCESS("저장", CREATED),
    UPDATE_SUCCESS("업데이트", OK),
    DELETE_SUCCESS("삭제", OK)
    ;
    private final String msg;
    private final HttpStatus httpStatus;


    Code(String msg, HttpStatus httpStatus) {
        this.msg = msg;
        this.httpStatus = httpStatus;
    }
}