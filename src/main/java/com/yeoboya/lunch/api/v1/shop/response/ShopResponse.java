package com.yeoboya.lunch.api.v1.shop.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ShopResponse {

    private String shopName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ItemResponse> items;

    public static ShopResponse from(Shop shop) {
        return new ShopResponse(shop.getName(), shop.getItems().stream()
                .map((r)->ItemResponse.of(r.getName(), r.getPrice()))
                .collect(Collectors.toList()));
    }

}