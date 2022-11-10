package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {

    private final Long id;
    private final String email;
    private final int price;
    private List<OrderItemResponse> orderItems;

    // 생성자 오버로딩
    public OrderResponse(Order order) {
        this.id = order.getId();
        this.email = order.getMember().getEmail();    //LAZY 초기화
        this.price = order.getTotalPrice();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }

    @Builder
    public OrderResponse(Long id, String email, int price) {
        this.id = id;
        this.email = email;
        this.price = price;
    }

}
