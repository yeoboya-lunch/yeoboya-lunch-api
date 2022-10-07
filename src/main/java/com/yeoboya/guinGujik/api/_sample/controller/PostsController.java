package com.yeoboya.guinGujik.api._sample.controller;

import com.yeoboya.guinGujik.api._sample.model.req.PostsCreateDto;
import com.yeoboya.guinGujik.api._sample.service.PostsService;
import com.yeoboya.guinGujik.config.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@Slf4j
@RestController
public class PostsController {

    private final PostsService service;
    private final Response response;

    public PostsController(PostsService service, Response response) {
        this.service = service;
        this.response = response;
    }

    // test/../PostsControllerTest.java or ./client.http 테스트
    @PostMapping("/posts")
    public ResponseEntity<?> post(@RequestBody @Valid PostsCreateDto postsCreateDto){
        return response.success(postsCreateDto, "데이터 조회 성공");
    }

}
