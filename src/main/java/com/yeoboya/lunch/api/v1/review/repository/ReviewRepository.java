package com.yeoboya.lunch.api.v1.review.repository;

import com.yeoboya.lunch.api.v1.review.domain.Review;
import com.yeoboya.lunch.api.v1.review.response.MonthlyReviewStats;
import com.yeoboya.lunch.api.v1.review.response.RatingDistribution;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    boolean existsByMemberAndOrder(Member member, Order order);

    @Query("SELECT r FROM Review r JOIN FETCH r.order JOIN FETCH r.member WHERE r.shop = :shop")
    List<Review> findByShop(@Param("shop") Shop shop, Sort sort);

//    // 특정 상점의 년/월별 평균 점수를 조회합니다.
//    @Query("SELECT new com.yeoboya.lunch.api.v1.review.response.MonthlyReviewStats(YEAR(r.order.orderDate), MONTH(r.order.orderDate), AVG(r.shopRating)) " +
//            "FROM Review r WHERE r.shop = :shop GROUP BY YEAR(r.order.orderDate), MONTH(r.order.orderDate)")
//    List<MonthlyReviewStats> findMonthlyAverageRatings(@Param("shop") Shop shop);
//
//    // 특정 상점의 전체 평균 점수를 조회합니다.
//    @Query("SELECT AVG(r.shopRating) FROM Review r WHERE r.shop = :shop")
//    Double findOverallAverageRating(@Param("shop") Shop shop);
//
//    // 특정 상점의 각 점수별 리뷰 개수를 조회합니다.
//    @Query("SELECT new com.yeoboya.lunch.api.v1.review.response.RatingDistribution(r.shopRating, COUNT(r)) " +
//            "FROM Review r WHERE r.shop = :shop GROUP BY r.shopRating")
//    List<RatingDistribution> findRatingDistribution(@Param("shop") Shop shop);
//
//    // 특정 상점의 총 리뷰 개수를 조회합니다.
//    @Query("SELECT COUNT(r) FROM Review r WHERE r.shop = :shop")
//    long countByShop(@Param("shop") Shop shop);
}
