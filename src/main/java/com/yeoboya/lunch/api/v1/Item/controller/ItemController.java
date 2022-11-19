package com.yeoboya.lunch.api.v1.Item.controller;

import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.Item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 아이템 등록
     */
    @PostMapping
    public ItemResponse create(@RequestBody @Valid ItemCreate create) {
        return itemService.saveItem(create);
    }

    /**
     * 아이템 리스트 조회
     */
    @GetMapping
    public List<ItemResponse> getList(Pageable pageable) {
        System.out.println("pageable = " + pageable);
        return itemService.getList(pageable);
    }

    /**
     * 아이템 단건 조회
     */
    @GetMapping("/{itemId}")
    public ItemResponse get(@PathVariable Long itemId) {
        return itemService.get(itemId);
    }

    /**
     * 아이템 수정
     */
    @PatchMapping("/{itemId}")
    public void updateItem(@PathVariable Long itemId, @RequestBody @Valid ItemEdit edit) {
        itemService.edit(itemId, edit);
    }

    /**
     * 아이템 삭제
     */
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId){
        itemService.delete(itemId);
    }

}