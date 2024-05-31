package com.yeoboya.lunch.api.v1.review.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReviewStatsResponse {
    private List<MonthlyReviewStats> monthlyStats;
    private Double overallAverageRating;
    private List<RatingDistribution> ratingDistribution;
    private long totalReviewCount;

}

