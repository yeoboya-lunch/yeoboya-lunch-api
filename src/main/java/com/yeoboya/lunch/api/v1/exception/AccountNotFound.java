package com.yeoboya.lunch.api.v1.exception;

/**
 * status -> 404
 */
public class AccountNotFound extends LunchException {

    private static final String MESSAGE = "계좌가 존재하지 않습니다.";

    public AccountNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
