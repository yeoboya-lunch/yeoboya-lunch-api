package com.yeoboya.lunch.api.v1.common.exception;

import lombok.Setter;

@Setter
public class AuthorityException extends LunchException {

    public AuthorityException(String message){
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 403; // HTTP status code for "Forbidden"
    }
}
