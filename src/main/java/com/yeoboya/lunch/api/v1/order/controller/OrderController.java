package com.yeoboya.lunch.api.v1.order.controller;


import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.ErrorCode;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.order.request.GroupOrderJoin;
import com.yeoboya.lunch.api.v1.order.request.OrderEdit;
import com.yeoboya.lunch.api.v1.order.request.OrderRecruitmentCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
import com.yeoboya.lunch.api.v1.order.response.OrderDetailResponse;
import com.yeoboya.lunch.api.v1.order.service.OrderService;
import com.yeoboya.lunch.config.annotation.Auth;
import com.yeoboya.lunch.config.security.JwtTokenProvider;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final Bucket bucket;
    private final Response response;
    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    public OrderController(Response response, OrderService orderService, JwtTokenProvider jwtTokenProvider) {
        this.response = response;
        this.orderService = orderService;
        this.jwtTokenProvider = jwtTokenProvider;

        //10분에 10개의 요청을 처리할 수 있는 Bucket 생성
        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(10)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * 점심 주문 모집
     */
    @Auth
    @PostMapping("/recruit")
    public ResponseEntity<Body> lunchOrderRecruit(@RequestBody @Valid OrderRecruitmentCreate orderRecruitmentCreate, HttpServletRequest request){
        String jwtTokenSubject = jwtTokenProvider.getJwtTokenSubject(request);
        System.out.println("jwtTokenSubject = " + jwtTokenSubject);

        if (bucket.tryConsume(1)) {
            OrderDetailResponse orderDetailResponse = orderService.lunchOrderRecruitWrite(orderRecruitmentCreate);
            return response.success(Code.SAVE_SUCCESS, orderDetailResponse);
        }
        System.out.println("TOO MANY REQUEST");
        return response.fail(ErrorCode.TOO_MANY_REQUESTS);

    }

    /**
     * 점심 주문 모집 리스트
     */
    @GetMapping("/recruits")
    public ResponseEntity<Body> lunchRecruits(OrderSearch search, Pageable pageable){
        return response.success(Code.SEARCH_SUCCESS, orderService.recruits(search, pageable));
    }

    /**
     * 주문번호로 점심 주문 조회
     */
    @GetMapping("/recruit/{orderId}")
    public ResponseEntity<Body> lunchRecruitByOrderId(@PathVariable Long orderId){
        return response.success(Code.SEARCH_SUCCESS, orderService.lunchRecruitByOrderId(orderId));
    }

    /**
     * 주문 요청하기
     */
    @PostMapping("/recruit/group/join")
    public ResponseEntity<Body> lunchRecruitsGroupJoin(@RequestBody @Valid GroupOrderJoin groupOrderJoin){
        orderService.lunchRecruitsJoin(groupOrderJoin);
        return response.success(Code.SEARCH_SUCCESS);
    }

    @DeleteMapping("/recruit/group/join/{groupOrderId}")
    public ResponseEntity<Body> lunchRecruitsGroupExit(@PathVariable Long groupOrderId){
        orderService.lunchRecruitsGroupExit(groupOrderId);
        return response.success(Code.SEARCH_SUCCESS);
    }

    /**
     * 주문모집 상태변경
     */
    @PatchMapping("/recruit/{orderId}")
    public ResponseEntity<Body> lunchRecruitStatus(@PathVariable(value = "orderId") Long orderId, @RequestBody OrderEdit orderEdit){
        orderService.lunchRecruitStatus(orderId, orderEdit);
        return response.success(Code.SEARCH_SUCCESS);
    }

}