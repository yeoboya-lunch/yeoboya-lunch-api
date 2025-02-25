package com.yeoboya.lunch.api.v1.shop.controller.specification;

import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.shop.request.ShopAndItemCreate;
import com.yeoboya.lunch.api.v1.shop.request.ShopCreate;
import com.yeoboya.lunch.api.v1.shop.request.ShopSearch;
import com.yeoboya.lunch.api.v1.shop.response.ShopResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Hidden // Swagger UI에서 숨김 (필요하면 제거 가능)
@Tag(name = "Shop", description = "상점 관리 API")
@RequestMapping("/shop")
public interface ShopApi {

    @Operation(summary = "상점 생성", description = "새로운 상점을 등록합니다.")
    @PostMapping
    ResponseEntity<Body> create(@RequestBody @Valid ShopCreate create);

    @Operation(summary = "상점 생성 및 아이템 추가", description = "상점을 생성하고 동시에 아이템(메뉴)을 추가합니다.")
    @PostMapping("/create")
    ResponseEntity<Body> createShopAndAddItem(@RequestBody @Valid ShopAndItemCreate create);

    @Operation(summary = "상점 조회", description = "등록된 상점을 조회합니다.")
    @GetMapping
    ResponseEntity<Body> shop(ShopSearch search, Pageable pageable);
}