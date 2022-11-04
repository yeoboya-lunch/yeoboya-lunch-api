package com.yeoboya.lunch.api.v1.order.controller;


import com.yeoboya.lunch.api.v1.order.reqeust.OrderEdit;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderSearch;
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
    public OrderResponse order(@RequestParam("email") String email, @RequestParam("itemId") Long itemId, @RequestParam("count") int count) {
        return orderService.order(email, itemId, count);
    }

    @GetMapping("/{orderId}")
    public OrderResponse get(@PathVariable String orderId) {
        return null;
    }

    @GetMapping("/list")
    public List<OrderResponse> getList(@ModelAttribute OrderSearch search) {
        return null;
    }

    @PatchMapping("/{postId}")
    public OrderResponse edit(@RequestBody @Valid OrderEdit edit) {
        return null;
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
    }


}
