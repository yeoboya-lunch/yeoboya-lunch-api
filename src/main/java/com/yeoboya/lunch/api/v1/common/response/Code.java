package com.yeoboya.lunch.api.v1.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Getter
public enum Code {



    SEARCH_SUCCESS(OK,"조회"),
    SAVE_SUCCESS(CREATED,"저장"),
    UPDATE_SUCCESS(OK,"업데이트"),
    DELETE_SUCCESS(OK,"삭제");

    private final HttpStatus httpStatus;
    private final String msg;

    Code(HttpStatus httpStatus, String msg) {
        this.httpStatus = httpStatus;
        this.msg = msg;
    }
}