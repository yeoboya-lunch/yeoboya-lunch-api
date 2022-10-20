package com.yeoboya.guinGujik.api.jpa.service;

import com.yeoboya.guinGujik.api.jpa.domain.Item;
import com.yeoboya.guinGujik.api.jpa.dto.ItemSearchCond;
import com.yeoboya.guinGujik.api.jpa.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);

}