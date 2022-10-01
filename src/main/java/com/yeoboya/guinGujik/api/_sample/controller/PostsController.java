package com.yeoboya.guinGujik.api._sample.controller;

import com.yeoboya.guinGujik.api._sample.model.req.PostsCreateDto;
import com.yeoboya.guinGujik.common.BasicResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@Slf4j
@RestController
public class PostsController {

    // test/../PostsControllerTest.java or ./PostsController.http 테스트
    @PostMapping("/posts")
    public String post(@RequestBody @Valid PostsCreateDto postsCreateDto){
        log.info("in /posts {}", postsCreateDto);
        return "";
    }

}
