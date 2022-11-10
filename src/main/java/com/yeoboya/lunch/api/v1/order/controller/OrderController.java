package com.yeoboya.lunch.api.v1.order.controller;


import com.yeoboya.lunch.api.v1.order.request.OrderCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderEdit;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
import com.yeoboya.lunch.api.v1.order.response.OrderItemResponse;
import com.yeoboya.lunch.api.v1.order.response.OrderResponse;
import com.yeoboya.lunch.api.v1.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse order(@RequestBody @Valid OrderCreate orderCreate){
        return orderService.order(orderCreate);
    }

    @GetMapping
    public void getCond() {
    }


    // 1:N
    @GetMapping("/list")
    public List<OrderResponse> getList(OrderSearch search, Pageable pageable) {
        return orderService.orderList(search, pageable);
    }

    @PatchMapping("/{orderId}")
    public OrderResponse edit(@RequestBody @Valid OrderEdit edit) {
        return null;
    }

    @PatchMapping("/cancel/{orderId}")
    public void cancel(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }


    // N:1
    @GetMapping("/item")
    public List<OrderItemResponse> getOrderItem(OrderSearch search, Pageable pageable){
        return orderService.orderItemList(search, pageable);
    }


}
