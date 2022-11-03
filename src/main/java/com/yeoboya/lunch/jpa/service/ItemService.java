package com.yeoboya.lunch.jpa.service;

import com.yeoboya.lunch.jpa.domain.Item;
import com.yeoboya.lunch.jpa.dto.ItemSearchCond;
import com.yeoboya.lunch.jpa.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);

}