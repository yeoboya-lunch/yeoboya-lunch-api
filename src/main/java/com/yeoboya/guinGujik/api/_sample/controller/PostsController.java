package com.yeoboya.guinGujik.api._sample.controller;

import com.yeoboya.guinGujik.api._sample.model.req.PostsCreateDto;
import com.yeoboya.guinGujik.api._sample.service.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Slf4j
@RestController
public class PostsController {

    private final PostsService service;

    public PostsController(PostsService service) {
        this.service = service;
    }

    // test/../PostsControllerTest.java or ./client.http 테스트
    @PostMapping("/posts")
    public String post(@RequestBody @Valid PostsCreateDto postsCreateDto){
        log.info("in /posts {}", postsCreateDto);
        return "";
    }

}
