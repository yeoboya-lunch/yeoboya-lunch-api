package com.yeoboya.lunch.api.v1.board.controller;

import com.yeoboya.lunch.api.v1.board.controller.specification.LikeApi;
import com.yeoboya.lunch.api.v1.board.service.LikeService;
import com.yeoboya.lunch.api.v1.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/like")
@RequiredArgsConstructor
public class LikeController implements LikeApi {

    private final LikeService likeService;

    @PostMapping("/{boardId}")
    public ResponseEntity<Response.Body> likePost(@PathVariable Long boardId) {
       return likeService.likePost(boardId);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Response.Body> unlikePost(@PathVariable Long boardId) {
        return likeService.unlikePost(boardId);
    }
}
