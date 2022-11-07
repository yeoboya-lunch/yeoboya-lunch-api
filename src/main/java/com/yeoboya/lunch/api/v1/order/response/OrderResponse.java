package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderResponse {

    private final Long id;
    private final String memberId;
    private final int price;
    private List<OrderItem> orderItemList;

    // 생성자 오버로딩
    public OrderResponse(Order order) {
        this.id = order.getId();
        this.memberId = order.getMember().getName();
        this.price = order.getTotalPrice();
        this.orderItemList = order.getOrderItems();
    }

    @Builder
    public OrderResponse(Long id, String memberId, int price) {
        this.id = id;
        this.memberId = memberId;
        this.price = price;
    }
}
