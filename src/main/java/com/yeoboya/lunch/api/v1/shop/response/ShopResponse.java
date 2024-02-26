package com.yeoboya.lunch.api.v1.shop.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ShopResponse {

    private String shopName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ItemResponse> items;

    public static ShopResponse from(Shop shop) {
        return new ShopResponse(
                shop.getName(),
                shop.getItems().stream().map((r) -> ItemResponse.of(r.getName(), r.getPrice())).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopResponse that = (ShopResponse) o;
        return Objects.equals(shopName, that.shopName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopName);
    }

}
