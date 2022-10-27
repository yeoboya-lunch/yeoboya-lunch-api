package com.yeoboya.guinGujik.api.v1._sample.service;

import com.yeoboya.guinGujik.api.v1._sample.repository.RetryRepository;
import com.yeoboya.guinGujik.config.annotation.Retry;
import com.yeoboya.guinGujik.config.annotation.Trace;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

    private static int seq = 0;

    private final RetryRepository repository;

    public RetryService(RetryRepository repository) {
        this.repository = repository;
    }

    // test/../PostsServiceTest.java  테스트
    @Trace
    @Retry(value = 4)
    public String read(String phoneNum) {
        seq++;
        if (seq % 5 == 0) {
            throw new IllegalStateException("예외발생");
        }else{
            repository.read(phoneNum);
        }
        return "ok";
    }


}
