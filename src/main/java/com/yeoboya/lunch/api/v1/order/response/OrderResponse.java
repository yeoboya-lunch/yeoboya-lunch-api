package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class OrderResponse {

    private final String orderName;
    private final OrderStatus orderStatus;
    private final int totalPrice;
    private final List<OrderItemResponse> orderItems;

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getMember().getName(), order.getStatus(), order.getTotalPrice(),
                order.getOrderItems().stream()
                        .map(OrderItemResponse::new)
                        .collect(Collectors.toList()));
    }

    @Builder
    public OrderResponse(String orderName, OrderStatus orderStatus, int totalPrice, Order orderItems) {
        this.orderName = orderName;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }

}