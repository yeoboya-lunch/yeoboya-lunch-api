package com.yeoboya.lunch.api.v1.order.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class OrderEdit {

    @NotBlank(message = "주문상태을 입력해주세요.")
    private String status;

}
