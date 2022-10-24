package com.yeoboya.guinGujik;

import com.yeoboya.guinGujik.config.annotation.ExcludeScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@EnableCaching
@SpringBootApplication
@ComponentScan(
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = {"com.yeoboya.guinGujik.api.*.*",}),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {ExcludeScan.class})
        }
)
public class GuinGujikApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuinGujikApplication.class, args);
    }

}
