package com.yeoboya.lunch.api.v1.order.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class GroupOrderJoin {

    @NotNull(message = "주분번호를 입력해주세요.")
    private Long orderId;

    @NotBlank(message = "주문자를 입력해주세요.")
    private String loginId;

    @NotNull(message = "상품정보를 입력해주세요.")
    private List<OrderItemCreate> orderItems;

    @Builder
    public GroupOrderJoin(Long orderId, String loginId, List<OrderItemCreate> orderItems) {
        this.orderId = orderId;
        this.loginId = loginId;
        this.orderItems = orderItems;
    }

}
