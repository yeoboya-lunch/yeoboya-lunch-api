package com.yeoboya.guinGujik.api._sample.service;

import com.yeoboya.guinGujik.api._sample.repository.PostsRepository;
import com.yeoboya.guinGujik.config.annotation.Retry;
import com.yeoboya.guinGujik.config.annotation.Trace;
import org.springframework.stereotype.Service;

@Service
public class PostsService {

    private static int seq = 0;

    private final PostsRepository repository;

    public PostsService(PostsRepository repository) {
        this.repository = repository;
    }

    // test/../PostsServiceTest.java  테스트
    @Trace
    @Retry(value = 4)
    public String read(String phoneNum) {
        seq++;
        if (seq % 5 == 0) {
            throw new IllegalStateException("에외발생");
        }else{
            repository.read(phoneNum);
        }
        return "ok";
    }


}
