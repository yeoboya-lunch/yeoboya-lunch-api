package com.yeoboya.lunch.api.v1.shop.service;

import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.repository.ShopRepository;
import com.yeoboya.lunch.api.v1.shop.request.ShopCreate;
import com.yeoboya.lunch.api.v1.shop.request.ShopSearch;
import com.yeoboya.lunch.api.v1.shop.response.ShopResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public ShopResponse create(ShopCreate create) {
        Shop shop = Shop.builder().
                name(create.getShopName()).
                build();
        return ShopResponse.from(shopRepository.save(shop));
    }

    public List<ShopResponse> shop(ShopSearch search, Pageable pageable) {
        return shopRepository.shopItem(search, pageable).stream()
                .map(ShopResponse::from)
                .collect(Collectors.toList());
    }
}
