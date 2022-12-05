package com.yeoboya.lunch;

import com.yeoboya.lunch.config.annotation.ExcludeScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@EnableJpaAuditing
@EnableBatchProcessing
@ComponentScan(
        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.yeoboya.lunch.config.datasources.*"),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {ExcludeScan.class})
        }
)
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}