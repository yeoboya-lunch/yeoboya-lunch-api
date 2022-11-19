package com.yeoboya.lunch.api.v1.order.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class OrderCreate {

    @NotBlank(message = "주문자를 입력해주세요.")
    private String name;

    @NotNull(message = "상점을 입력해주세요.")
    private String shopName;

    @NotNull(message = "상품정보를 입력해주세요.")
    private List<OrderItemCreate> orderItems;

    @Builder
    public OrderCreate(String name, String shopName, List<OrderItemCreate> orderItems) {
        this.name = name;
        this.shopName = shopName;
        this.orderItems = orderItems;
    }
}