package com.yeoboya.lunch.api.v1.review.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReviewRequest {
    private Long orderId;

    @NotBlank
    private String content;

    @Min(0)
    @Max(5)
    private int shopRating = 5;

}
