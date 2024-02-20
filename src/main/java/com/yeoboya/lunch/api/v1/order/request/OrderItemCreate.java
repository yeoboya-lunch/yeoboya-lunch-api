package com.yeoboya.lunch.api.v1.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class OrderItemCreate {

    @NotNull(message = "메뉴를 입력해주세요.")
    private String itemName;

    @Builder.Default
    @NotNull(message = "주문 수량을 입력해주세요.")
    private int orderQuantity = 1;
}
