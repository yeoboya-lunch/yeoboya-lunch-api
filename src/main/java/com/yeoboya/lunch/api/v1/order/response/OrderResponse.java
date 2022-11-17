package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {


    private final Long id;
    private final String orderName;
    private final int totalPrice;
    private final List<OrderItemResponse> orderItems;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderName = order.getMember().getName();
        this.totalPrice = order.getTotalPrice();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }

    @Builder
    public OrderResponse(Long id, String orderName, int totalPrice, Order order) {
        this.id = id;
        this.orderName = orderName;
        this.totalPrice = totalPrice;
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }

}
