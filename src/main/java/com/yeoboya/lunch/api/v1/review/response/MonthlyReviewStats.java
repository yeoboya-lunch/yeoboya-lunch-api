package com.yeoboya.lunch.api.v1.review.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyReviewStats {
    private int year;
    private int month;
    private double averageRating;
}
