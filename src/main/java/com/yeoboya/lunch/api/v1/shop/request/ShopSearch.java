package com.yeoboya.lunch.api.v1.shop.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class ShopSearch {

    private String shopName;



    //fixme 상점 검색 ?shopName= 없을 경우  NullPointerException 처리
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopSearch that = (ShopSearch) o;
        return getShopName().equals(that.getShopName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getShopName());
    }
}
