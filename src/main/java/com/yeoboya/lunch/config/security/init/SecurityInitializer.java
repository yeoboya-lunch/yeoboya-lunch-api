package com.yeoboya.lunch.config.security.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityInitializer implements ApplicationRunner {

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        log.debug("start run");
    }
}
