package com.yeoboya.lunch.batch.job;


import com.yeoboya.lunch.config.annotation.ExcludeScan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
@ExcludeScan
public class TaskletJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job orderJob2(){
        Job exampleJob = jobBuilderFactory.get("orderJob2")
                .start(orderStep2())
                .build();

        return exampleJob;
    }

    @Bean
    public Step orderStep2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step 222!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
