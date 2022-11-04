package com.yeoboya.lunch.api.v1.order.reqeust;

import lombok.Data;

@Data
public class OrderSearchCond {

    private String itemName;
    private Integer maxPrice;

    public OrderSearchCond() {
    }

    public OrderSearchCond(String itemName, Integer maxPrice) {
        this.itemName = itemName;
        this.maxPrice = maxPrice;
    }

}