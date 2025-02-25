package com.yeoboya.lunch.api.v1.Item.controller.specification;

import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import com.yeoboya.lunch.api.v1.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Item", description = "아이템 관리 API")
@RequestMapping("/item")
public interface ItemApi {

    @Operation(summary = "아이템 등록")
    @PostMapping
    ResponseEntity<Response.Body> create(@RequestBody @Valid ItemCreate create);

    @Operation(summary = "아이템 리스트 조회")
    @Deprecated
    @GetMapping("/shop/{shopName}")
    ResponseEntity<Response.Body> getItemsByShop(@PathVariable String shopName, Pageable pageable);

    @Operation(summary = "아이템 단건 조회")
    @GetMapping("/{itemId}")
    ResponseEntity<Response.Body> get(@PathVariable Long itemId);

    @Operation(summary = "아이템 수정")
    @PatchMapping("/{itemId}")
    ResponseEntity<Response.Body> updateItem(@PathVariable Long itemId, @RequestBody @Valid ItemEdit edit);

    @Operation(summary = "아이템 삭제")
    @DeleteMapping("/{itemId}")
    ResponseEntity<Response.Body> deleteItem(@PathVariable Long itemId);
}