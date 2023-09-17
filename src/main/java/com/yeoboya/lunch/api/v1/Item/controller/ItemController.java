package com.yeoboya.lunch.api.v1.Item.controller;

import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.Item.service.ItemService;
import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.ErrorCode;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.common.service.PricingPlanService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
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
    private final PricingPlanService pricingPlanService;


    /**
     * 아이템 등록
     */
    @PostMapping
    public ResponseEntity<Body> create(@RequestBody @Valid ItemCreate create) {
        ItemResponse itemResponse = itemService.saveItem(create);
        return response.success(Code.SAVE_SUCCESS, itemResponse);
    }

    /**
     * 아이템 리스트 조회
     */
    @GetMapping
    public ResponseEntity<Body> getList(Pageable pageable) {
        List<ItemResponse> itemResponses = itemService.getList(pageable);
        return response.success(Code.SEARCH_SUCCESS, itemResponses);
    }

    /**
     * 아이템 단건 조회
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Body> get(@PathVariable Long itemId) {
        Bucket bucket = pricingPlanService.resolveBucket("");
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        long saveToekn = probe.getRemainingTokens();

        if (probe.isConsumed()) {
            ItemResponse itemResponse = itemService.get(itemId);
            return response.success(Code.SEARCH_SUCCESS, itemResponse);
        }

        long waitForRefill = probe.getNanosToWaitForRefill();
        System.out.println("TOO MANY REQUEST");
        System.out.println("Available Toekn : " + saveToekn);
        System.out.println("Wait Time " + waitForRefill + "Second");
        return response.fail(ErrorCode.TOO_MANY_REQUESTS);
    }

    /**
     * 아이템 수정
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Body> updateItem(@PathVariable Long itemId, @RequestBody @Valid ItemEdit edit) {
        itemService.edit(itemId, edit);
        return response.success(Code.UPDATE_SUCCESS);
    }

    /**
     * 아이템 삭제
     */
    //fixme OrderItem cascade
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Body> deleteItem(@PathVariable Long itemId){
        itemService.delete(itemId);
        return response.success(Code.DELETE_SUCCESS.getMsg());
    }


    //todo 아이템 조회 상점이름, 메뉴, 가격
}
