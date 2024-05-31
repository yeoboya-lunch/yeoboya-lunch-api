package com.yeoboya.lunch.api.v1.review.repository;

import com.yeoboya.lunch.api.v1.review.domain.Review;
import com.yeoboya.lunch.api.v1.review.response.MonthlyReviewStats;
import com.yeoboya.lunch.api.v1.review.response.RatingDistribution;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<MonthlyReviewStats> findMonthlyAverageRatings(Shop shop);
    Double findOverallAverageRating(Shop shop);
    List<RatingDistribution> findRatingDistribution(Shop shop);
    long countByShop(Shop shop);
    List<Review> findByShopWithFetchJoin(Shop shop, Sort sort);

}
