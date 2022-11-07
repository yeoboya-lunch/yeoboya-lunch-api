package com.yeoboya.lunch.api.v1.order.service;

import com.yeoboya.lunch.api.v1.order.reqeust.OrderCreate;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderEdit;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderSearch;
import com.yeoboya.lunch.api.v1.order.response.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderResponse order(OrderCreate orderCreate);

    void update(Long itemId, OrderEdit orderEdit);

    Optional<OrderService> findById(Long id);

    List<OrderResponse> findItems(OrderSearch OrderSearch);

}
