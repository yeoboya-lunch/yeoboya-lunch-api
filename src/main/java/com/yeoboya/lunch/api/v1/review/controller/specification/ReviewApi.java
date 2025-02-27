package com.yeoboya.lunch.api.v1.review.controller.specification;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.review.request.ReviewRequest;
import com.yeoboya.lunch.api.v1.review.request.ReviewUpdateRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Review", description = "리뷰 관리 API")
@RequestMapping("/reviews")
public interface ReviewApi {

    @Operation(summary = "리뷰 추가", description = "새로운 리뷰를 작성합니다.")
    @PostMapping
    ResponseEntity<Response.Body> addReview(@RequestBody @Valid ReviewRequest reviewRequest);

    @Operation(summary = "리뷰 수정", description = "기존 리뷰를 수정합니다.")
    @PutMapping("/{reviewId}")
    ResponseEntity<Response.Body> updateReview(@PathVariable Long reviewId, @RequestBody @Valid ReviewUpdateRequest reviewRequest);

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    ResponseEntity<Response.Body> deleteReview(@PathVariable Long reviewId);

    @Operation(summary = "리뷰 통계 조회", description = "특정 상점의 리뷰 통계를 조회합니다.")
    @GetMapping("/stats/{shopId}")
    ResponseEntity<Response.Body> getReviewStatistics(@PathVariable Long shopId);

    @Operation(summary = "정렬된 리뷰 조회", description = "특정 상점의 리뷰를 정렬하여 조회합니다.")
    @GetMapping("/{shopId}/sorted")
    ResponseEntity<Response.Body> getSortedReviews(@PathVariable Long shopId, @RequestParam String sortBy, @RequestParam String order);
}