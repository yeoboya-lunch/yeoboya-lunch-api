package com.yeoboya.lunch.api.v1.Item.service;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.Item.repository.ItemRepository;
import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import com.yeoboya.lunch.api.v1.Item.request.ItemEditor;
import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.repository.ShopRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ShopRepository shopRepository;

    public ItemService(ItemRepository itemRepository, ShopRepository shopRepository) {
        this.itemRepository = itemRepository;
        this.shopRepository = shopRepository;
    }

    public ItemResponse saveItem(ItemCreate create) {
        Shop findShop = shopRepository.findByName(create.getShopName()).orElseThrow(
                ()->new EntityNotFoundException("Shop not found - " + create.getShopName()));
        Item createItem = Item.builder().
                shop(findShop).
                name(create.getItemName()).
                price(create.getPrice()).
                build();
        Item item = itemRepository.save(createItem);
        return ItemResponse.from(item);
    }

    public ItemResponse get(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                ()->new EntityNotFoundException("Item not found - " + itemId));
        return ItemResponse.builder()
                .id(itemId)
                .shopName(item.getShop().getName())
                .name(item.getName())
                .price(item.getPrice())
                .build();
    }

    public List<ItemResponse> getList(Pageable pageable) {
        return itemRepository.getList(pageable).stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public void edit(Long itemId, ItemEdit itemEdit) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                ()->new EntityNotFoundException("Item not found - " + itemId));

        ItemEditor.ItemEditorBuilder editorBuilder = item.toEditor();

        ItemEditor itemEditor = editorBuilder.itemName(itemEdit.getName())
                .price(itemEdit.getPrice())
                .build();

        item.edit(itemEditor);
    }

    public void delete(Long id){
        itemRepository.deleteById(id);
    }

}