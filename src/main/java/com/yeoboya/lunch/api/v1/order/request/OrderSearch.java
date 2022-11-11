package com.yeoboya.lunch.api.v1.order.request;

import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@ToString
public class OrderSearch {

    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date endDate;

    private String orderName;
    private OrderStatus orderStatus;
    private Integer orderQuantity;
    private Integer orderPrice;

}
