package com.yeoboya.lunch.api.v1.order.request;

import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class GroupOrderSearch {

    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate endDate;

    private String orderEmail;
    private String orderName;
    private OrderStatus orderStatus;

}
