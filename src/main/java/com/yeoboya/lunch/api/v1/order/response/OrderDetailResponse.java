package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class OrderDetailResponse {

    private long orderId;
    private String title;
    private String lastOrderTime;
    private String orderStatus;
    private String memo;
    private int deliveryFee;
    private List<GroupOrderResponse> joinMember;

    public static OrderDetailResponse of(Order order) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 a HH:mm");
        long orderId = order.getId();
        String formattedLastOrderTime = simpleDateFormat.format(order.getLastOrderTime());
        String title = order.getTitle();
        String statusTitle = order.getStatus().getTitle();
        String memo = order.getMemo();
        int deliveryFee = order.getDeliveryFee();

        List<GroupOrderResponse> groupOrderResponses = order.getGroupOrders().stream()
                .map(r -> GroupOrderResponse.from(r, r.getMember(), r.getOrderItems()))
                .collect(Collectors.toList());

        return new OrderDetailResponse(orderId, title, formattedLastOrderTime, statusTitle, memo, deliveryFee, groupOrderResponses);
    }
}
