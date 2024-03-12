package com.yeoboya.lunch.api.v1.Item.repository;

import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ItemRepositoryCustom {

    Map<String, List<ItemResponse>> getItemsByShop(String shopName, Pageable pageable);
}
