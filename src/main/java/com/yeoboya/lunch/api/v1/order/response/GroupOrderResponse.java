package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.order.domain.GroupOrder;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GroupOrderResponse {

    //todo 내 주문내역, 모집내역 return 분리 하기
    private Long orderId;
    private Long groupOrderId;
    private String title;
    private String email;
    private String name;
    private List<OrderItemResponse> orderItem;
    private int totalPrice;

    public static GroupOrderResponse of(GroupOrder groupOrder, Member member, List<OrderItem> orderItems) {
        GroupOrderResponse groupOrderResponse = new GroupOrderResponse();
        groupOrderResponse.setGroupOrderId(groupOrder.getId());
        groupOrderResponse.setOrderId(groupOrder.getOrder().getId());
        groupOrderResponse.setTitle(groupOrder.getOrder().getTitle());
        groupOrderResponse.setEmail(member.getEmail());
        groupOrderResponse.setName(member.getName());
        groupOrderResponse.setOrderItem(orderItems.stream().map(OrderItemResponse::new).collect(Collectors.toList()));
        groupOrderResponse.setTotalPrice(orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum());
        return groupOrderResponse;
    }

    public static GroupOrderResponse of(GroupOrder groupOrder) {
        GroupOrderResponse groupOrderResponse = new GroupOrderResponse();
        groupOrderResponse.setGroupOrderId(groupOrder.getId());
        groupOrderResponse.setOrderId(groupOrder.getOrder().getId());
        groupOrderResponse.setTitle(groupOrder.getOrder().getTitle());
        groupOrderResponse.setEmail(groupOrder.getMember().getEmail());
        groupOrderResponse.setName(groupOrder.getMember().getName());

        List<OrderItem> orderItems = groupOrder.getOrderItems();
        groupOrderResponse.setOrderItem(orderItems.stream().map(OrderItemResponse::new).collect(Collectors.toList()));
        groupOrderResponse.setTotalPrice(totalPriceCalculate(orderItems));

        return groupOrderResponse;
    }

    private static int totalPriceCalculate(List<OrderItem> orderItems) {
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

}
