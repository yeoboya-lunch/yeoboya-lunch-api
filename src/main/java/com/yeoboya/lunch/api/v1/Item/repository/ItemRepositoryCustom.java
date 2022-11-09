package com.yeoboya.lunch.api.v1.Item.repository;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> getList(Pageable pageable);
}
