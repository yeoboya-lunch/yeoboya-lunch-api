package com.yeoboya.lunch.api.v1.shop.request;

import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@Getter
@ToString
public class ShopAndItemCreate {

    @NotBlank(message = "상점 이름을 입력해주세요.")
    private String shopName;

    private List<ItemCreate> items;
}
