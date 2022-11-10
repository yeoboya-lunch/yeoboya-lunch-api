package com.yeoboya.lunch.api.v1.shop.repository;

import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.request.ShopSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopRepositoryCustom {

    List<Shop> shopItem(ShopSearch search, Pageable pageable);

}
