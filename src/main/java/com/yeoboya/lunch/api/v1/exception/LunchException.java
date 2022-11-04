package com.yeoboya.lunch.api.v1.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class LunchException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public LunchException(String message) {
        super(message);
    }

    public LunchException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
