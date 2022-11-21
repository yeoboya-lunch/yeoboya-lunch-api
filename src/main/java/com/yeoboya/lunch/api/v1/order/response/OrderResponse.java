package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {

    private final String orderName;
    private final OrderStatus orderStatus;
    private final int totalPrice;
    private final List<OrderItemResponse> orderItems;

    public OrderResponse(Order order) {
        this.orderName = order.getMember().getName();
        this.orderStatus = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }

    @Builder
    public OrderResponse(String orderName, OrderStatus orderStatus, int totalPrice, Order order) {
        this.orderName = orderName;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }

}