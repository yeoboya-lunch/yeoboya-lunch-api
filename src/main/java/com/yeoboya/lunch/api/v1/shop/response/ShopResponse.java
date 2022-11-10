package com.yeoboya.lunch.api.v1.shop.response;

import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ShopResponse {

    private String shopName;
    private List<ItemResponse> itemResponses;


    public ShopResponse(Shop shop) {
        this.shopName = shop.getName();
        this.itemResponses = shop.getItems().stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }
}
