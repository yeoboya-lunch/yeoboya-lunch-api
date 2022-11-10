package com.yeoboya.lunch.api.v1.order.request;

import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderSearch {

    private OrderStatus orderStatus;
    private Integer orderQuantity;
    private Integer orderPrice;

    @Override
    public String toString() {
        return "OrderSearch{" +
                "orderStatus='" + orderStatus + '\'' +
                ", orderQuantity=" + orderQuantity +
                ", orderPrice=" + orderPrice +
                '}';
    }
}
