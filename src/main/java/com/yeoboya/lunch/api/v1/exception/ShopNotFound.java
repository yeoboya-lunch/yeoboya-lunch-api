package com.yeoboya.lunch.api.v1.exception;

/**
 * status -> 404
 */
public class ShopNotFound extends LunchException {

    private static final String MESSAGE = "존재하지 않는 식당입니다.";

    public ShopNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
