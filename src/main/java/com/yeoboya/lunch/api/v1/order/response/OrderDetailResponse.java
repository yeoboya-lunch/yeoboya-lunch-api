package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.response.MemberResponse;
import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderDetailResponse {

    private String orderName;
    private MemberResponse memberResponse;

    private String shopName;

    private String title;
    private String lastOrderTime;
    private OrderStatus orderStatus;
    private int deliveryFee;

    private int totalPrice;
    private List<OrderItemResponse> orderItems;

    public OrderDetailResponse(String title, String lastOrderTime, OrderStatus status, int deliveryFee) {
        this.title = title;
        this.lastOrderTime = lastOrderTime;
        this.orderStatus = status;
        this.deliveryFee = deliveryFee;
    }

    public OrderDetailResponse(Member member) {
        this.memberResponse = MemberResponse.from(member);
    }


    public static OrderDetailResponse orderInfo(Order order) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 a HH:mm");
        return new OrderDetailResponse(
                order.getTitle(),
                simpleDateFormat.format(order.getLastOrderTime()),
                order.getStatus(),
                order.getDeliveryFee());
//                order.getTotalPrice(),
//                order.getOrderItems().stream()
//                        .map(OrderItemResponse::new)
//                        .collect(Collectors.toList()));
    }

    public static OrderDetailResponse orderMemberInfo(Order order) {
        return new OrderDetailResponse(order.getMember());
    }



    @Builder
    public OrderDetailResponse(String orderName, String shopName, String title, String lastOrderTime, OrderStatus orderStatus, int deliveryFee, int totalPrice, Order orderItems) {
        this.orderName = orderName;
        this.shopName = shopName;
        this.title = title;
        this.lastOrderTime = lastOrderTime;
        this.orderStatus = orderStatus;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }


}