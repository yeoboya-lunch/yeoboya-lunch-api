package com.yeoboya.lunch.api.v1.common.exception;

/**
 * status -> 404
 */
public class OrderNotFound extends LunchException {

    private static final String MESSAGE = "존재하지 않는 주문입니다.";

    public OrderNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}