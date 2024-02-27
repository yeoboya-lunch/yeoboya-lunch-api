package com.yeoboya.lunch.api.v1.board.controller;

import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import com.yeoboya.lunch.api.v1.board.request.FileBoardCreate;
import com.yeoboya.lunch.api.v1.board.service.BoardService;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 작성
     */
    @PostMapping("/write")
    public ResponseEntity<Body> create(@RequestBody @Valid BoardCreate boardCreate) {
        return boardService.saveBoard(boardCreate);
    }

    /**
     * 게시글 작성 (파일첨부)
     */
    @PostMapping(value = "/write/photo", consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<Body> createPhoto(@RequestPart MultipartFile file, @RequestPart @Valid FileBoardCreate fileBoardCreate) {
        return boardService.saveBoardPhoto(file, fileBoardCreate);
    }

    /**
     * 게시글 조회
     */
    @GetMapping
    public ResponseEntity<Body> list(BoardSearch boardSearch, Pageable pageable) {
        return boardService.list(boardSearch, pageable);
    }

    /**
     * 게시글 단건 조회
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<Body> findBoardById(@PathVariable Long boardId) {
        return boardService.findBoardById(boardId);
    }
}
