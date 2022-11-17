package com.yeoboya.lunch.api.v1.order.controller;


import com.yeoboya.lunch.api.v1.order.request.OrderCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderEdit;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
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

    /** 주문 */
    @PostMapping
    public OrderResponse order(@RequestBody @Valid OrderCreate orderCreate){
        return orderService.order(orderCreate);
    }

    /** 주문내역 */
    @GetMapping("/list")
    public List<OrderResponse> getList(OrderSearch search, Pageable pageable) {
        return orderService.orderList(search, pageable);
    }

    /** 주문수정 */
    //todo 주문수정 기능
    @PatchMapping("/{orderId}")
    public void updateOrder(@PathVariable Long orderId, @RequestBody @Valid OrderEdit edit) {
        orderService.updateOrder(orderId, edit);
    }

    /** 주문취소 */
    @PatchMapping("/cancel/{orderId}")
    public void cancel(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }

}
