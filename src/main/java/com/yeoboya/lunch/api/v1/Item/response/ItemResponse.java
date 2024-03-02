package com.yeoboya.lunch.api.v1.Item.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeoboya.lunch.api.v1.Item.domain.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ItemResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final String shopName;
    private final String name;
    private final int price;


    public static ItemResponse from(Item item) {
        return new ItemResponse(item.getShop().getName(), item.getName(), item.getPrice());
    }

    public static ItemResponse of(String name, int price){
        return new ItemResponse("", name, price);
    }
}
