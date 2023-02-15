package com.yeoboya.lunch.api.v1.order.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class OrderStart {

    @NotBlank(message = "주문자를 입력해주세요.")
    private String email;

    @NotNull(message = "상점을 입력해주세요.")
    private String shopName;

    @NotNull(message = "상품정보를 입력해주세요.")
    private List<OrderItemCreate> orderItems;

    @Builder
    public OrderStart(String email, String shopName, List<OrderItemCreate> orderItems) {
        this.email = email;
        this.shopName = shopName;
        this.orderItems = orderItems;
    }
}