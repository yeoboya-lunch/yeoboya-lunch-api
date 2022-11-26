package com.yeoboya.lunch.api.v1.order.controller;


import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.order.request.OrderCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderEdit;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
import com.yeoboya.lunch.api.v1.order.response.OrderResponse;
import com.yeoboya.lunch.api.v1.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final Response response;
    private final OrderService orderService;


    /**
     * 주문
     */
    @PostMapping
    public ResponseEntity<Body> order(@RequestBody @Valid OrderCreate orderCreate){
        OrderResponse orderResponse = orderService.order(orderCreate);
        return response.success(Code.SAVE_SUCCESS, orderResponse);
    }

    /**
     * 주문내역
     */
    @GetMapping("/list")
    public ResponseEntity<Body> getList(OrderSearch search, Pageable pageable) {
        List<OrderResponse> orderResponses = orderService.orderList(search, pageable);
        return response.success(Code.SEARCH_SUCCESS, orderResponses);
    }

    /** 주문취소 */
    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<Body> cancel(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return response.success(Code.UPDATE_SUCCESS.getMsg());
    }

    /** 주문수정 */
    //todo 주문수정 기능
    @PatchMapping("/{orderId}")
    public void updateOrder(@PathVariable Long orderId, @RequestBody @Valid OrderEdit edit) {
        orderService.updateOrder(orderId, edit);
    }

}