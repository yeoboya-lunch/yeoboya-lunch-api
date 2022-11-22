package com.yeoboya.lunch.api.v1.Item.response;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponse {

    private final Long id;
    private final String shopName;
    private final String name;
    private final int price;

    public static ItemResponse from(Item item) {
        return new ItemResponse(item.getId(), item.getShop().getName(), item.getName(), item.getPrice());
    }
}
