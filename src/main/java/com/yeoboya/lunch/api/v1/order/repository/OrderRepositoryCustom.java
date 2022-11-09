package com.yeoboya.lunch.api.v1.order.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> orderList(OrderSearch orderSearch, Pageable pageable);

//    BooleanExpression likeItemName(String itemName);

    BooleanExpression maxPrice(Integer maxPrice);

    List<OrderItem> orderItemList(OrderSearch search);
}
