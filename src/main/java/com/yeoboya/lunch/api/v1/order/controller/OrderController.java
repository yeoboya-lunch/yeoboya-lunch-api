package com.yeoboya.lunch.api.v1.order.controller;


import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.ErrorCode;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.order.request.OrderRecruitmentCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
import com.yeoboya.lunch.api.v1.order.response.OrderResponse;
import com.yeoboya.lunch.api.v1.order.service.OrderService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final Bucket bucket;
    private final Response response;
    private final OrderService orderService;

    public OrderController(Response response, OrderService orderService) {
        this.response = response;
        this.orderService = orderService;

        //10분에 10개의 요청을 처리할 수 있는 Bucket 생성
        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(10)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * 점심 주문 모집
     */
    @PostMapping("/recruit")
    public ResponseEntity<Body> lunchOrderRecruit(@RequestBody @Valid OrderRecruitmentCreate orderRecruitmentCreate){


        System.out.println("orderRecruitmentCreate = " + orderRecruitmentCreate);

        if (bucket.tryConsume(1)) {
            OrderResponse orderResponse = orderService.lunchOrderRecruitWrite(orderRecruitmentCreate);
            return response.success(Code.SAVE_SUCCESS, orderResponse);
        }
        System.out.println("TOO MANY REQUEST");
        return response.fail(ErrorCode.TOO_MANY_REQUESTS);

    }

    @GetMapping("/recruits")
    public ResponseEntity<Body> lunchRecruits(OrderSearch search, Pageable pageable){
        return response.success(Code.SEARCH_SUCCESS, orderService.recruits(search, pageable));
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

}