package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;


@Getter
public class OrderRecruitmentResponse {

    @Builder
    public OrderRecruitmentResponse(Long orderId, String orderMemberName, String shopName, String title, String lastOrderTime, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderMemberName = orderMemberName;
        this.shopName = shopName;
        this.title = title;
        this.lastOrderTime = lastOrderTime;
        this.orderStatus = orderStatus;
    }

    private final Long orderId;
    private final String orderMemberName;
    private final String shopName;
    private final String title;
    private final String lastOrderTime;
    private final OrderStatus orderStatus;


    public static OrderRecruitmentResponse from(Order order) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss");
        return new OrderRecruitmentResponse(
                order.getId(),
                order.getMember().getName(),
                order.getShop().getName(),
                order.getTitle(),
                simpleDateFormat.format(order.getLastOrderTime()),
                order.getStatus()
        );
    }

//    @Builder
//    public OrderRecruitmentResponse(String orderName, OrderStatus orderStatus, int totalPrice, Order orderItems) {
//        this.orderName = orderName;
//        this.orderStatus = orderStatus;
//        this.totalPrice = totalPrice;
//        this.orderItems = orderItems.getOrderItems().stream()
//                .map(OrderItemResponse::new)
//                .collect(Collectors.toList());
//    }

}