package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {

    private final Long id;
    private final String itemName;
    private final int orderPrice;  //주문 가격
    private final int orderQuantity; //주문 수량

    public OrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.itemName = orderItem.getItem().getName();
        this.orderPrice = orderItem.getOrderPrice();
        this.orderQuantity = orderItem.getOrderQuantity();
    }
}