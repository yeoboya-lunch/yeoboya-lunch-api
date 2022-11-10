package com.yeoboya.lunch.api.v1.shop.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class ShopCreate {

    @NotBlank(message = "상점 이름을 입력해주세요.")
    private String shopName;

}
