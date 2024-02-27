package com.yeoboya.lunch.api.v1.board.controller;

import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import com.yeoboya.lunch.api.v1.board.request.ReplyCreateRequest;
import com.yeoboya.lunch.api.v1.board.service.ReplyService;
import com.yeoboya.lunch.api.v1.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/reply")
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 댓글작성
     */
    @PostMapping("/write")
    public ResponseEntity<Response.Body> createComment(@RequestBody ReplyCreateRequest replyCreateRequest) {
        return replyService.createReply(replyCreateRequest);
    }

    /**
     * 댓글조회
     */
    @GetMapping
    public ResponseEntity<Response.Body> getReplyForBoard(BoardSearch boardSearch, Pageable pageable) {
        return replyService.getReplyForBoard(boardSearch, pageable);
    }

    /**
     * 대댓글조회
     */
    @GetMapping("/children")
    public ResponseEntity<Response.Body> getChildrenForReply(BoardSearch boardSearch, Pageable pageable) {
        return replyService.getChildrenForReply(boardSearch, pageable);
    }
}
