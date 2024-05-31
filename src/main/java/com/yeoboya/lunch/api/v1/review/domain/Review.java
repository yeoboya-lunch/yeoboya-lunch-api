package com.yeoboya.lunch.api.v1.review.domain;

import com.yeoboya.lunch.api.v1.common.domain.BaseEntity;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"MEMBER_ID", "ORDER_ID"})
})
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOP_ID")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String content;

    @Min(0)
    @Max(5)
    @Column(nullable = false)
    private int shopRating;

    @Builder
    public Review(Member member, Shop shop, Order order, String content, int shopRating) {
        this.member = member;
        this.shop = shop;
        this.order = order;
        this.content = content;
        this.shopRating = shopRating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", member=" + member +
                ", shop=" + shop +
                ", order=" + order +
                ", content='" + content + '\'' +
                ", shopRating=" + shopRating +
                '}';
    }

}
