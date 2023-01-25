package com.yeoboya.lunch.api.v1.Item.request;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemEdit {

    private String name;
    private int price;

    @Builder
    public ItemEdit(String name, int price) {
        this.name = name;
        this.price = price;
    }

}
