package com.yeoboya.lunch.api.v1.common.exception;

import lombok.Setter;

@Setter
public class EntityNotFoundException extends LunchException {

    private String message = "404";

    public EntityNotFoundException(String message){
        super(message);
        this.message = message;
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

    //todo -> Supplier<X>

}
