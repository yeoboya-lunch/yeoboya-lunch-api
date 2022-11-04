package com.yeoboya.lunch.api.v1.Item.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ItemCreate {

    @NotBlank(message = "상품을 입력해주세요.")
    private String name;

    @NotNull(message = "가격을 입력해주세요.")
    private int price;

    @Builder
    public ItemCreate(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
