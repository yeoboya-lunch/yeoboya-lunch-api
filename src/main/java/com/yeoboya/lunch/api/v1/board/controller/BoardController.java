package com.yeoboya.lunch.api.v1.board.controller;

import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.board.request.FileBoardCreate;
import com.yeoboya.lunch.api.v1.board.service.BoardService;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
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

    @PostMapping(value = "/write/files", consumes = {"multipart/form-data"})
    public ResponseEntity<Body> createFile(@ModelAttribute FileBoardCreate fileBoardCreate){
        log.warn("{}", fileBoardCreate);
        return boardService.saveBoardFile(fileBoardCreate);
    }

}
