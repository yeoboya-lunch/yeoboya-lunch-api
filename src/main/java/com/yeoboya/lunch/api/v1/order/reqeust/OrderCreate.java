package com.yeoboya.lunch.api.v1.order.reqeust;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class OrderCreate {

    @NotBlank(message = "주문자를 입력해주세요.")
    private String email;

    @NotNull(message = "아이템 번호를 입력해주세요.")
    private int itemId;

    @NotNull(message = "주문 수량을 입력해주세요.")
    @Builder.Default
    private int orderQuantity = 1;

    @Builder
    public OrderCreate(String email, int itemId, int orderQuantity) {
        this.email = email;
        this.itemId = itemId;
        this.orderQuantity = orderQuantity;
    }

}
