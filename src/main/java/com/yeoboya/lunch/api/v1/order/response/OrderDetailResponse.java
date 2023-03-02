package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;

@Getter
@Setter
public class OrderDetailResponse {

    private String title;
    private String lastOrderTime;
    private OrderStatus orderStatus;
    private String memo;
    private int deliveryFee;

    public OrderDetailResponse(String title, String lastOrderTime, OrderStatus orderStatus, String memo, int deliveryFee) {
        this.title = title;
        this.lastOrderTime = lastOrderTime;
        this.orderStatus = orderStatus;
        this.memo = memo;
        this.deliveryFee = deliveryFee;
    }

    public static OrderDetailResponse orderInfo(Order order){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 a HH:mm");
        return new OrderDetailResponse(
                order.getTitle(),
                simpleDateFormat.format(order.getLastOrderTime()),
                order.getStatus(),
                order.getMemo(),
                order.getDeliveryFee());
    }
}