package com.yeoboya.guinGujik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
//@ComponentScan(
//        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.REGEX, pattern = {"com.yeoboya.guinGujik.api.*.*",}),
//                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {ExcludeScan.class})
//        }
//)
public class GuinGujikApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuinGujikApplication.class, args);
    }

}
