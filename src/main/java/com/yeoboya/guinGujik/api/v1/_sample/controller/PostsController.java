package com.yeoboya.guinGujik.api.v1._sample.controller;

import com.yeoboya.guinGujik.api.v1._sample.model.req.PostsCreateDto;
import com.yeoboya.guinGujik.config.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/posts")
public class PostsController {

    private final Response response;

    public PostsController(Response response) {
        this.response = response;
    }


    // test/../PostsControllerTest.java or ./PostsController.http 테스트
    @PostMapping
    public ResponseEntity<?> post(@RequestBody @Valid PostsCreateDto postsCreateDto){
        log.info("in /posts {}", postsCreateDto);
        return response.success("성공");
    }

}
