package com.yeoboya.guinGujik.jpa.service;

import com.yeoboya.guinGujik.jpa.domain.Item;
import com.yeoboya.guinGujik.jpa.dto.ItemSearchCond;
import com.yeoboya.guinGujik.jpa.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);

}