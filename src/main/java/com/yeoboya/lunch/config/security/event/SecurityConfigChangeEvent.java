package com.yeoboya.lunch.config.security.event;

import org.springframework.context.ApplicationEvent;

public class SecurityConfigChangeEvent extends ApplicationEvent {
    public SecurityConfigChangeEvent(Object source) {
        super(source);
    }
}
