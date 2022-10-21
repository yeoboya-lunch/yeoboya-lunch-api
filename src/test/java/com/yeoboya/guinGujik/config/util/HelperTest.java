package com.yeoboya.guinGujik.config.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;


@SpringBootTest
class HelperTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
        if (applicationContext != null) {
            Helper.printBeanNames(applicationContext);
        }
    }
}