package com.yeoboya.lunch.api.v1.Item.repository;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.Item.request.ItemSearch;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> getList(ItemSearch itemSearch);
}
