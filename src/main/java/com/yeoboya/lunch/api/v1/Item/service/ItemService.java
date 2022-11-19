package com.yeoboya.lunch.api.v1.Item.service;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.Item.repository.ItemRepository;
import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import com.yeoboya.lunch.api.v1.Item.request.ItemEditor;
import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.common.exception.ItemNotFound;
import com.yeoboya.lunch.api.v1.common.exception.ShopNotFound;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ShopRepository shopRepository;

    public ItemResponse saveItem(ItemCreate create) {
        Shop findShop = shopRepository.findByName(create.getShopName()).orElseThrow(ShopNotFound::new);
        Item createItem = Item.builder().
                shop(findShop).
                name(create.getItemName()).
                price(create.getPrice()).
                build();
        return new ItemResponse(itemRepository.save(createItem));
    }

    public ItemResponse get(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFound::new);
        return ItemResponse.builder()
                .id(itemId)
                .shopName(item.getShop().getName())
                .name(item.getName())
                .price(item.getPrice())
                .build();
    }

    public List<ItemResponse> getList(Pageable pageable) {
        return itemRepository.getList(pageable).stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void edit(Long id, ItemEdit itemEdit) {
        Item item = itemRepository.findById(id).orElseThrow(ItemNotFound::new);

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