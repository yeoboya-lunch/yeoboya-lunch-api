package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.List;

@Getter
@Setter
public class OrderDetailResponse {

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

    public static OrderDetailResponse orderInfo(Order order){
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


}