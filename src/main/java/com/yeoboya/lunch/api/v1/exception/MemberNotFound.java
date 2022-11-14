package com.yeoboya.lunch.api.v1.exception;

/**
 * status -> 404
 */
public class MemberNotFound extends LunchException {

    private static final String MESSAGE = "존재하지 않는 이름입니다.";

    public MemberNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
