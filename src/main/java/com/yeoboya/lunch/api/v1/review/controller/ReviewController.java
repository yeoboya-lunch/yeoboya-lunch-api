package com.yeoboya.lunch.api.v1.review.controller;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.review.request.ReviewRequest;
import com.yeoboya.lunch.api.v1.review.request.ReviewUpdateRequest;
import com.yeoboya.lunch.api.v1.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Response.Body> addReview(@RequestBody ReviewRequest reviewRequest) {
        return reviewService.addReview(reviewRequest);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Response.Body> updateReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateRequest reviewRequest) {
        return reviewService.updateReview(reviewId, reviewRequest);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Response.Body> deleteReview(@PathVariable Long reviewId) {
        return reviewService.deleteReview(reviewId);
    }

    @GetMapping("/stats/{shopId}")
    public ResponseEntity<Response.Body> getReviewStatistics(@PathVariable Long shopId) {
        return reviewService.getReviewStatistics(shopId);
    }

    @GetMapping("/{shopId}/sorted")
    public ResponseEntity<Response.Body> getSortedReviews(@PathVariable Long shopId, @RequestParam String sortBy, @RequestParam String order) {
        return reviewService.getSortedReviews(shopId, sortBy, order);
    }


    // 기타 엔드포인트들...
}
