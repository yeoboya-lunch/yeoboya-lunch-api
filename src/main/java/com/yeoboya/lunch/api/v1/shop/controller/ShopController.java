package com.yeoboya.lunch.api.v1.shop.controller;

import com.yeoboya.lunch.api.v1.common.response.Body;
import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.shop.request.ShopCreate;
import com.yeoboya.lunch.api.v1.shop.request.ShopSearch;
import com.yeoboya.lunch.api.v1.shop.response.ShopResponse;
import com.yeoboya.lunch.api.v1.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final Response response;
    private final ShopService service;

    /**
     * 상점 생성
     */
    @PostMapping
    public ResponseEntity<Body> create(@RequestBody @Valid ShopCreate create) {
        ShopResponse shopResponse = service.create(create);
        return response.success(shopResponse, Code.SAVE_SUCCESS.getMsg());
    }

    /**
     * 상점 조회
     */
    @GetMapping
    public ResponseEntity<Body> shop(ShopSearch search, Pageable pageable) {
        List<ShopResponse> shopResponses = service.shop(search, pageable);
        return response.success(shopResponses, Code.SAVE_SUCCESS.getMsg(), Code.SAVE_SUCCESS.getHttpStatus());
    }

}