package com.yeoboya.lunch.api.v1.board.controller.specification;

import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "게시글 좋아요 API")
public interface LikeApi {

    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요를 추가합니다.")
    @PostMapping("/{boardId}")
    ResponseEntity<Body> likePost(@PathVariable Long boardId);

    @Operation(summary = "게시글 좋아요 취소", description = "게시글에 추가한 좋아요를 취소합니다.")
    @DeleteMapping("/{boardId}")
    ResponseEntity<Body> unlikePost(@PathVariable Long boardId);
}