package com.yeoboya.lunch.api.v1.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderItemCreate {

    @NotNull(message = "메뉴를 입력해주세요.")
    private String itemName;

    @NotNull(message = "주문 수량을 입력해주세요.")
    @Builder.Default
    private int orderQuantity = 1;
}
