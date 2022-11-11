package com.yeoboya.lunch.api.v1.order.repository;

import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> orderList(OrderSearch orderSearch, Pageable pageable);

    List<OrderItem> orderItems(Long orderID);

    @Deprecated
    List<OrderItem> orderItemList(OrderSearch search, Pageable pageable);


}
