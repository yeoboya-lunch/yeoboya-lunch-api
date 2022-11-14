package com.yeoboya.lunch.api.v1.Item.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ItemEdit {

    private String name;
    private int price;

    @Builder
    public ItemEdit(String name, int price) {
        this.name = name;
        this.price = price;
    }

}
