package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.config.security.event.SecurityConfigChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityConfigChangePublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishSecurityConfigChanged() {
        SecurityConfigChangeEvent event = new SecurityConfigChangeEvent(this);
        eventPublisher.publishEvent(event);
    }
}
