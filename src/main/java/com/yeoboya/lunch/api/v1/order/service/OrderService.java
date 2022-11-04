package com.yeoboya.lunch.api.v1.order.service;

import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderEdit;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderSearchCond;
import com.yeoboya.lunch.api.v1.order.response.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderResponse order(String email, Long itemId, int count);

    void update(Long itemId, OrderEdit orderEdit);

    Optional<OrderService> findById(Long id);

    List<Order> findItems(OrderSearchCond orderSearchCond);

}
