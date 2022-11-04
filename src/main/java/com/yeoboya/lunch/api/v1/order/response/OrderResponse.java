package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponse {

    private final Long id;
    private final String memberId;
    private final int price;

    // 생성자 오버로딩
    public OrderResponse(Order order) {
        this.id = order.getId();
        this.memberId = order.getMember().getName();
        this.price = order.getTotalPrice();
    }

    @Builder
    public OrderResponse(Long id, String memberId, int price) {
        this.id = id;
        this.memberId = memberId;
        this.price = price;
    }
}
