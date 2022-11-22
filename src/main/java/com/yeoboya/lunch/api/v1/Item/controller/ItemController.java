package com.yeoboya.lunch.api.v1.Item.controller;

import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.Item.service.ItemService;
import com.yeoboya.lunch.api.v1.common.response.Body;
import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final Response response;
    private final ItemService itemService;

    /**
     * 아이템 등록
     */
    @PostMapping
    public ResponseEntity<Body> create(@RequestBody @Valid ItemCreate create) {
        ItemResponse itemResponse = itemService.saveItem(create);
        return response.success(itemResponse, Code.SAVE_SUCCESS);
    }

    /**
     * 아이템 리스트 조회
     */
    @GetMapping
    public ResponseEntity<Body> getList(Pageable pageable) {
        List<ItemResponse> itemResponses = itemService.getList(pageable);
        return response.success(itemResponses, Code.SEARCH_SUCCESS);
    }

    /**
     * 아이템 단건 조회
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Body> get(@PathVariable Long itemId) {
        ItemResponse itemResponse = itemService.get(itemId);
        return response.success(itemResponse, Code.SEARCH_SUCCESS);
    }

    /**
     * 아이템 수정
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Body> updateItem(@PathVariable Long itemId, @RequestBody @Valid ItemEdit edit) {
        itemService.edit(itemId, edit);
        return response.success(Code.UPDATE_SUCCESS.getMsg());
    }

    /**
     * 아이템 삭제
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Body> deleteItem(@PathVariable Long itemId){
        itemService.delete(itemId);
        return response.success(Code.DELETE_SUCCESS.getMsg());
    }


    //fixme 아이템 조회 상점이름, 메뉴, 가격
}