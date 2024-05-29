package com.yeoboya.lunch.api.v1.order.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GroupOrderJoinEdit {

    private Long orderId;
    private Long groupOrderId;

    @NotNull(message = "상품정보를 입력해주세요.")
    private List<OrderItemCreateEdit> orderItems;

}
