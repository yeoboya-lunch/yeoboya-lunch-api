package com.yeoboya.guinGujik.api.jpa.controller;

import com.yeoboya.guinGujik.api.jpa.domain.Item;
import com.yeoboya.guinGujik.api.jpa.dto.ItemSearchCond;
import com.yeoboya.guinGujik.api.jpa.dto.ItemUpdateDto;
import com.yeoboya.guinGujik.api.jpa.service.ItemService;
import com.yeoboya.guinGujik.config.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final Response response;


    @GetMapping
    public ResponseEntity<?> items(@ModelAttribute("itemSearch") ItemSearchCond itemSearch) {
        List<Item> items = itemService.findItems(itemSearch);
        return response.success(items, "아이템 조회");
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> item(@PathVariable long itemId) {
        Optional<Item> item = itemService.findById(itemId);
        return response.success(item, "아이템 개별 조회");
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestBody Item item) {
        Item savedItem = itemService.save(item);
        return response.success(savedItem, "아이템 등록");
    }

    @PostMapping("/{itemId}/edit")
    public ResponseEntity<?> edit(@PathVariable Long itemId, @RequestBody ItemUpdateDto updateParam) {
        itemService.update(itemId, updateParam);
        return response.success("아이템 수정");
    }

}