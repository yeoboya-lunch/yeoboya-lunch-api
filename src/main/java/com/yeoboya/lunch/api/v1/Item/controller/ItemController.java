package com.yeoboya.lunch.api.v1.Item.controller;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import com.yeoboya.lunch.api.v1.Item.request.ItemSearch;
import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.Item.service.ItemService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item create(@RequestBody @Valid ItemCreate create) {
        return itemService.saveItem(create);
    }

    @GetMapping("/{itemId}")
    public ItemResponse get(@PathVariable Long itemId) {
        return itemService.get(itemId);
    }

    @PatchMapping("/{itemId}")
    public void updateItem(@PathVariable Long itemId, @RequestBody @Valid ItemEdit edit) {
        itemService.edit(itemId, edit);
    }

    @GetMapping
    public List<ItemResponse> getList(@ModelAttribute ItemSearch itemSearch) {
        return itemService.getList(itemSearch);
    }

}
