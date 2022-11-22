package com.yeoboya.lunch.api.v1.common.exception;

import lombok.Setter;

@Setter
public class EntityNotFoundException extends LunchException {

    public EntityNotFoundException(String message){
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
