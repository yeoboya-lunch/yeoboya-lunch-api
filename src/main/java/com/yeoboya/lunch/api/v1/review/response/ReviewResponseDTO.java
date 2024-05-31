package com.yeoboya.lunch.api.v1.review.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReviewResponseDTO {
    private String shopName;
    private List<ReviewSummaryDTO> reviews;

    @Data
    @AllArgsConstructor
    public static class ReviewSummaryDTO {
        private Long reviewId;
        private Long orderId;
        private String content;
        private int shopRating;

    }

}
