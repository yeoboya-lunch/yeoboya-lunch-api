package com.yeoboya.lunch.api.v1.common.exception;

import lombok.Setter;

@Setter
public class TooManyRequestsException extends LunchException {

    public TooManyRequestsException(String message){
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 429;
    }

}
