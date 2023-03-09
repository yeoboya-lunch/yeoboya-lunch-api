package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
public class OrderRecruitmentResponse {

    @Builder
    public OrderRecruitmentResponse(Long orderId, String orderMemberEmail, String orderMemberName, String shopName, String title, String lastOrderTime, String orderStatus, int groupCount) {
        this.orderId = orderId;
        this.orderMemberEmail = orderMemberEmail;
        this.orderMemberName = orderMemberName;
        this.shopName = shopName;
        this.title = title;
        this.lastOrderTime = lastOrderTime;
        this.orderStatus = orderStatus;
        this.groupCount = groupCount;
    }

    private final Long orderId;
    private final String orderMemberEmail;
    private final String orderMemberName;
    private final String shopName;
    private final String title;
    private final String lastOrderTime;
    private final String orderStatus;
    private final int groupCount;

    public static OrderRecruitmentResponse from(Order order) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 a HH:mm");
        return new OrderRecruitmentResponse(
                order.getId(),
                order.getMember().getEmail(),
                order.getMember().getName(),
                order.getShop().getName(),
                order.getTitle(),
                simpleDateFormat.format(order.getLastOrderTime()),
                order.getStatus().getTitle(),
                order.getGroupOrders().size()
        );
    }
}