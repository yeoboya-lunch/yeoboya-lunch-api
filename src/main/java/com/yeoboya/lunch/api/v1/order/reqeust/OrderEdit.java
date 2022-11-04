package com.yeoboya.lunch.api.v1.order.reqeust;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class OrderEdit {

    @NotBlank(message = "상품을 입력해주세요.")
    private String itemName;

    @NotBlank(message = "가격을 입력해주세요.")
    private int price;

    @NotBlank(message = "수량을 입력해주세요.")
    private int stockQuantity;

    @Builder
    public OrderEdit(String itemName, int price, int stockQuantity) {
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

}
