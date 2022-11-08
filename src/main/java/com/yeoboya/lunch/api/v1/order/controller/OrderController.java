package com.yeoboya.lunch.api.v1.order.controller;


import com.yeoboya.lunch.api.v1.order.reqeust.OrderCreate;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderEdit;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderSearch;
import com.yeoboya.lunch.api.v1.order.response.OrderItemResponse;
import com.yeoboya.lunch.api.v1.order.response.OrderResponse;
import com.yeoboya.lunch.api.v1.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/list")
    public List<OrderResponse> getList(@ModelAttribute OrderSearch search) {
        return orderService.orderList(search);
    }

    @PatchMapping("/{postId}")
    public OrderResponse edit(@RequestBody @Valid OrderEdit edit) {
        return null;
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
    }

    @GetMapping("/item")
    public List<OrderItemResponse> getOrderItem(@ModelAttribute OrderSearch search){
        List<OrderItemResponse> orderItems = orderService.orderItemList(search);
        return orderItems;
    }


}
