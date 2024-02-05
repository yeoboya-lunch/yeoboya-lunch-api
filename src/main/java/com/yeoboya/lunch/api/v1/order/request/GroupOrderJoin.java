package com.yeoboya.lunch.api.v1.order.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class GroupOrderJoin {

    private Long orderNo;

    @NotBlank(message = "주문자를 입력해주세요.")
    private String email;

    @NotNull(message = "상품정보를 입력해주세요.")
    private List<OrderItemCreate> orderItems;


}
