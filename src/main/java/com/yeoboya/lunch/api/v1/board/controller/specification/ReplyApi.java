package com.yeoboya.lunch.api.v1.board.controller.specification;

import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import com.yeoboya.lunch.api.v1.board.request.ReplyCreateRequest;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Reply", description = "게시글 댓글 API")
public interface ReplyApi {

    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @PostMapping("/write")
    ResponseEntity<Body> createComment(@RequestBody @Valid ReplyCreateRequest replyCreateRequest);

    @Operation(summary = "댓글 조회", description = "특정 게시글의 댓글 목록을 조회합니다.")
    @GetMapping
    ResponseEntity<Body> fetchBoardReplies(BoardSearch boardSearch, Pageable pageable);

    @Operation(summary = "대댓글 조회", description = "특정 댓글의 대댓글 목록을 조회합니다.")
    @GetMapping("/children")
    ResponseEntity<Body> fetchReplyChildren(BoardSearch boardSearch, Pageable pageable);
}