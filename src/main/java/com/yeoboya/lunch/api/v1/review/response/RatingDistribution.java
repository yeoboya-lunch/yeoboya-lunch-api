package com.yeoboya.lunch.api.v1.review.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingDistribution {
    private int rating;
    private long count;
}
