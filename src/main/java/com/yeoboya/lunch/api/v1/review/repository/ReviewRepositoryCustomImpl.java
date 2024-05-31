package com.yeoboya.lunch.api.v1.review.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.review.domain.QReview;
import com.yeoboya.lunch.api.v1.review.domain.Review;
import com.yeoboya.lunch.api.v1.review.response.MonthlyReviewStats;
import com.yeoboya.lunch.api.v1.review.response.RatingDistribution;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MonthlyReviewStats> findMonthlyAverageRatings(Shop shop) {
        QReview review = QReview.review;

        List<Tuple> results = queryFactory
                .select(review.order.orderDate.year(), review.order.orderDate.month(), review.shopRating.avg())
                .from(review)
                .where(review.shop.eq(shop))
                .groupBy(review.order.orderDate.year(), review.order.orderDate.month())
                .fetch();

        return results.stream()
                .map(tuple -> new MonthlyReviewStats(
                        tuple.get(review.order.orderDate.year()),
                        tuple.get(review.order.orderDate.month()),
                        tuple.get(review.shopRating.avg())))
                .collect(Collectors.toList());
    }

    @Override
    public Double findOverallAverageRating(Shop shop) {
        QReview review = QReview.review;

        return queryFactory
                .select(review.shopRating.avg())
                .from(review)
                .where(review.shop.eq(shop))
                .fetchOne();
    }

    @Override
    public List<RatingDistribution> findRatingDistribution(Shop shop) {
        QReview review = QReview.review;

        List<Tuple> results = queryFactory
                .select(review.shopRating, review.shopRating.count())
                .from(review)
                .where(review.shop.eq(shop))
                .groupBy(review.shopRating)
                .fetch();

        return results.stream()
                .map(tuple -> new RatingDistribution(tuple.get(review.shopRating), tuple.get(review.shopRating.count())))
                .collect(Collectors.toList());
    }

    @Override
    public long countByShop(Shop shop) {
        QReview review = QReview.review;

        return queryFactory
                .select(review.count())
                .from(review)
                .where(review.shop.eq(shop))
                .fetchOne();
    }


    @Override
    public List<Review> findByShopWithFetchJoin(Shop shop, Sort sort) {
        QReview review = QReview.review;

        return queryFactory
                .selectFrom(review)
                .join(review.order).fetchJoin()
                .join(review.member).fetchJoin()
                .where(review.shop.eq(shop))
                .orderBy(getOrderSpecifier(sort, review))
                .fetch();
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort sort, QReview review) {
        Sort.Order order = sort.iterator().next();
        switch (order.getProperty()) {
            case "shopRating":
                return order.isAscending() ? review.shopRating.asc() : review.shopRating.desc();
            case "orderDate":
                return order.isAscending() ? review.order.orderDate.asc() : review.order.orderDate.desc();
            default:
                throw new IllegalArgumentException("Unsupported sort property: " + order.getProperty());
        }
    }

}
