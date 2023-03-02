package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GroupOrderResponse {

    private String email;
    private String name;
    private List<OrderItemResponse> orderItem;
    private int totalPrice;

    public static GroupOrderResponse from(Member member, List<OrderItem> orderItems) {
        GroupOrderResponse groupOrderResponse = new GroupOrderResponse();
        groupOrderResponse.setEmail(member.getEmail());
        groupOrderResponse.setName(member.getName());
        groupOrderResponse.setOrderItem(orderItems.stream().map(OrderItemResponse::new).collect(Collectors.toList()));
        groupOrderResponse.setTotalPrice(orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum());
        return groupOrderResponse;
    }


}
