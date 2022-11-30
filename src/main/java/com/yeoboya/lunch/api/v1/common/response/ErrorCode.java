package com.yeoboya.lunch.api.v1.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    INVALID_REFRESH_TOKEN("리프레시 토큰이 유효하지 않습니다", BAD_REQUEST),
//    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),

    INVALID_AUTH_TOKEN("권한 정보가 없는 토큰입니다", UNAUTHORIZED),
//    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),

//    MEMBER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
//    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),

    USER_DUPLICATE_EMAIL("사용중인 이메일 입니다.", CONFLICT),
    USER_NOT_FOUND("아이디 또는 비밀번호를 잘못 입력했습니다", NOT_FOUND),

    INVALID_PASSWORD("비밀번호가 일치하지 않습니다. 다시 확인해 주세요.", BAD_REQUEST),
    INVALID_OLD_PASSWORD("Old password isn't valid", BAD_REQUEST),

    DUPLICATE_RESOURCE("데이터가 이미 존재합니다", CONFLICT),

    INVALID_PASSWORD_RESET_LINK("잘못된 비밀번호 재설정 링크를 클릭했습니다", BAD_REQUEST)
    ;

    private final String msg;
    private final HttpStatus httpStatus;


    ErrorCode(String msg, HttpStatus httpStatus) {
        this.msg = msg;
        this.httpStatus = httpStatus;
    }
}