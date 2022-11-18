package com.yeoboya.lunch.api.v1.Item.response;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemResponse {

    private final String name;
    private final int price;

    // 생성자 오버로딩
    public ItemResponse(Item item) {
        this.name = item.getName();
        this.price = item.getPrice();
    }

    @Builder
    public ItemResponse(String name, int price) {
        this.name = name;
        this.price = price;
    }

}
