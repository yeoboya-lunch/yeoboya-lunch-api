package com.yeoboya.lunch.api.v1.shop.controller;

import com.yeoboya.lunch.api.v1.shop.request.ShopCreate;
import com.yeoboya.lunch.api.v1.shop.request.ShopSearch;
import com.yeoboya.lunch.api.v1.shop.response.ShopResponse;
import com.yeoboya.lunch.api.v1.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService service;

    /**
     * 상점 생성
     */
    @PostMapping
    public ShopResponse create(@RequestBody @Valid ShopCreate create) {
        return service.create(create);
    }

    /**
     * 상점 조회
     */
    @GetMapping
    public List<ShopResponse> shop(ShopSearch search, Pageable pageable) {
        return service.shop(search, pageable);
    }

}