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
public class GroupOrderJoinEdit {


    @NotBlank(message = "주문자를 입력해주세요.")
    @Email(message = "Valid email is required.")
    private String email;

    @NotNull(message = "상품정보를 입력해주세요.")
    private List<OrderItemCreateEdit> orderItems;

    @Builder
    public GroupOrderJoinEdit(String email, List<OrderItemCreateEdit> orderItems) {
        this.email = email;
        this.orderItems = orderItems;
    }

}
