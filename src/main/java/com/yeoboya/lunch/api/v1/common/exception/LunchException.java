package com.yeoboya.lunch.api.v1.common.exception;

import lombok.Getter;

@Getter
public abstract class LunchException extends RuntimeException {

    public LunchException(String message) {
        super(message);
    }
    public abstract int getStatusCode();

}