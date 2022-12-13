package com.yeoboya.lunch.api.v1.board.controller;

import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.board.service.BoardService;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final Response response;
    private final BoardService boardService;


    /**
     * 게시글 작성
     */
    @PostMapping("/write")
    public ResponseEntity<Body> create(@RequestBody @Valid BoardCreate boardCreate){
        return boardService.saveBoard(boardCreate);
    }

}
