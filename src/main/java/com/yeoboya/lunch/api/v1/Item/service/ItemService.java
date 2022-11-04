package com.yeoboya.lunch.api.v1.Item.service;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.Item.domain.ItemEditor;
import com.yeoboya.lunch.api.v1.Item.repository.ItemRepository;
import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import com.yeoboya.lunch.api.v1.Item.request.ItemSearch;
import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.exception.ItemNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item saveItem(ItemCreate create) {
        Item createItem = Item.builder().
                name(create.getName()).
                price(create.getPrice()).

                build();
        return itemRepository.save(createItem);
    }

    @Transactional
    public void edit(Long id, ItemEdit itemEdit) {
        Item item = itemRepository.findById(id)
                .orElseThrow(ItemNotFound::new);

        ItemEditor.ItemEditorBuilder editorBuilder = item.toEditor();

        ItemEditor itemEditor = editorBuilder.itemName(itemEdit.getName())
                .price(itemEdit.getPrice())
                .build();

        item.edit(itemEditor);
    }


    public ItemResponse get(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFound::new);

        return ItemResponse.builder().id(itemId).name(item.getName()).price(item.getPrice()).build();
    }



    public List<ItemResponse> getList(ItemSearch itemSearch) {
        return itemRepository.getList(itemSearch).stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }
}
