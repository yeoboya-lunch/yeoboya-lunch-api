package com.yeoboya.lunch.api.v1.Item.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemSearch {

    private String name;
    private int price;

}
