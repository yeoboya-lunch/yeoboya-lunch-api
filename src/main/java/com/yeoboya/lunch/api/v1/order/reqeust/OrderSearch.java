package com.yeoboya.lunch.api.v1.order.reqeust;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderSearch {

    private Integer orderQuantity;
    private Integer orderPrice;
}
