package com.yeoboya.lunch.api.v1.Item.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeoboya.lunch.api.v1.Item.domain.Item;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemResponse {

    private Long itemId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String shopName;
    private String name;
    private int price;

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .itemId(item.getId())
                .shopName(item.getShop().getName())
                .name(item.getName())
                .price(item.getPrice())
                .build();
    }

    public static ItemResponse of(String name, int price){
        return ItemResponse.builder()
                .name(name)
                .price(price)
                .build();
    }
}
