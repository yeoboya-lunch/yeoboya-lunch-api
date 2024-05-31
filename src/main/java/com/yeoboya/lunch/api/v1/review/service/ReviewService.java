package com.yeoboya.lunch.api.v1.review.service;

import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.order.domain.GroupOrder;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.repository.OrderRepository;
import com.yeoboya.lunch.api.v1.review.domain.Review;
import com.yeoboya.lunch.api.v1.review.repository.ReviewRepository;
import com.yeoboya.lunch.api.v1.review.request.ReviewRequest;
import com.yeoboya.lunch.api.v1.review.request.ReviewUpdateRequest;
import com.yeoboya.lunch.api.v1.review.response.MonthlyReviewStats;
import com.yeoboya.lunch.api.v1.review.response.RatingDistribution;
import com.yeoboya.lunch.api.v1.review.response.ReviewResponseDTO;
import com.yeoboya.lunch.api.v1.review.response.ReviewStatsResponse;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.repository.ShopRepository;
import com.yeoboya.lunch.config.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yeoboya.lunch.api.v1.common.response.Code.*;
import static com.yeoboya.lunch.api.v1.common.response.ErrorCode.DUPLICATE_RESOURCE;
import static com.yeoboya.lunch.api.v1.common.response.ErrorCode.INVALID_AUTH_TOKEN;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final Response response;

    @Transactional
    public ResponseEntity<Response.Body> addReview(ReviewRequest reviewRequest) {
        // 주문 조회
        Order order = orderRepository.findById(reviewRequest.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found - " + reviewRequest.getOrderId()));

        // 현재 사용자가 주문한 멤버 중 하나인지 확인
        Member currentUser = order.getGroupOrders().stream()
                .map(GroupOrder::getMember)
                .filter(member -> SecurityUtils.isCurrentUser(member.getEmail()))
                .findFirst()
                .orElse(null);

        if (currentUser == null) {
            return response.fail(INVALID_AUTH_TOKEN, "주문한 사람만 리뷰를 쓸 수 있습니다.");
        }

        // 해당 멤버가 이미 리뷰를 작성했는지 확인
        boolean reviewExists = reviewRepository.existsByMemberAndOrder(currentUser, order);

        if (reviewExists) {
            return response.fail(DUPLICATE_RESOURCE, "This member has already written a review for this order");
        }

        // 리뷰 생성
        Review review = Review.builder()
                .member(currentUser)
                .shop(order.getShop())
                .order(order)
                .content(reviewRequest.getContent())
                .shopRating(reviewRequest.getShopRating())
                .build();

        // 리뷰 저장
        Review saveReview = reviewRepository.save(review);

        return response.success(SAVE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Response.Body> updateReview(Long reviewId, ReviewUpdateRequest reviewRequest) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found - " + reviewId));

        // 현재 사용자가 리뷰 작성자인지 확인
        if (!SecurityUtils.isCurrentUser(review.getMember().getEmail())) {
            return response.fail(INVALID_AUTH_TOKEN, "정상적인 방법으로 리뷰를 수정해주세요");
        }

        // 리뷰 수정
        review.setContent(reviewRequest.getContent());
        review.setShopRating(reviewRequest.getShopRating());

        // 리뷰 저장
        reviewRepository.save(review);

        return response.success(UPDATE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Response.Body> deleteReview(Long reviewId) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found - " + reviewId));

        // 현재 사용자가 리뷰 작성자인지 확인
        if (!SecurityUtils.isCurrentUser(review.getMember().getEmail())) {
            return response.fail(INVALID_AUTH_TOKEN, "정상적인 방법으로 리뷰를 삭제해주세요");
        }

        // 리뷰 삭제
        reviewRepository.delete(review);

        return response.success(DELETE_SUCCESS);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Response.Body> getReviewStatistics(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found - " + shopId));

        List<MonthlyReviewStats> monthlyStats = reviewRepository.findMonthlyAverageRatings(shop);
        Double overallAverage = reviewRepository.findOverallAverageRating(shop);
        List<RatingDistribution> ratingDistribution = reviewRepository.findRatingDistribution(shop);
        long totalReviewCount = reviewRepository.countByShop(shop);

        ReviewStatsResponse statsResponse = new ReviewStatsResponse(monthlyStats, overallAverage, ratingDistribution, totalReviewCount);

        return response.success(statsResponse);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Response.Body> getSortedReviews(Long shopId, String sortBy, String order) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found - " + shopId));

        Sort sort;
        if ("desc".equalsIgnoreCase(order)) {
            sort = Sort.by(Sort.Order.desc(sortBy));
        } else {
            sort = Sort.by(Sort.Order.asc(sortBy));
        }

        List<Review> reviews = reviewRepository.findByShopWithFetchJoin(shop, sort);
        List<ReviewResponseDTO.ReviewSummaryDTO> reviewSummaryDTOs = reviews.stream()
                .map(review -> new ReviewResponseDTO.ReviewSummaryDTO(
                        review.getId(),
                        review.getOrder().getId(),
                        review.getContent(),
                        review.getShopRating()))
                .collect(Collectors.toList());

        ReviewResponseDTO shopReviewResponseDTO = new ReviewResponseDTO(shop.getName(), reviewSummaryDTOs);

        return response.success(shopReviewResponseDTO);
    }
}
