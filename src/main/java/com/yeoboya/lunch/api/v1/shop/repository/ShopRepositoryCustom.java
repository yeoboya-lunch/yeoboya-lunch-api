package com.yeoboya.lunch.api.v1.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.request.ShopSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ShopRepositoryCustom {

//    @Cacheable(cacheNames = "cacheShop")
    Slice<Shop> pageShops(ShopSearch search, Pageable pageable);

    List<Shop> shops(ShopSearch search, Pageable pageable);

    JPAQuery<Long> shopCounts(ShopSearch search);

}
