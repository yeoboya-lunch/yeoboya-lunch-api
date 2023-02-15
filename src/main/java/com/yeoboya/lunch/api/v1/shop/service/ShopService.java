package com.yeoboya.lunch.api.v1.shop.service;

import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.repository.ShopRepository;
import com.yeoboya.lunch.api.v1.shop.request.ShopCreate;
import com.yeoboya.lunch.api.v1.shop.request.ShopSearch;
import com.yeoboya.lunch.api.v1.shop.response.ShopResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public Map<String, Object> shop(ShopSearch search, Pageable pageable) {
        Slice<Shop> shops = shopRepository.pageShops(search, pageable);
        List<ShopResponse> content = shops.getContent().stream()
                .map(ShopResponse::from)
                .collect(Collectors.toList());

        return Map.of(
                "list", content,
                "isFirst", shops.isFirst(),
                "isLast", shops.isLast(),
                "hasNext", shops.hasNext(),
                "hasPrevious", shops.hasPrevious(),
                "pageNo", shops.getNumber()+1
        );
    }
}
