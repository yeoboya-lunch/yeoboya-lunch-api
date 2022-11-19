package com.yeoboya.lunch.api.v1.common.exception;

/**
 * status -> 404
 */
public class ItemNotFound extends LunchException {

    private static final String MESSAGE = "존재하지 않는 메뉴입니다.";

    public ItemNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}