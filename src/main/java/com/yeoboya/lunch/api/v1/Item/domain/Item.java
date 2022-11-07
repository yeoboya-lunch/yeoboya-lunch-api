package com.yeoboya.lunch.api.v1.Item.domain;

import com.yeoboya.lunch.api.v1.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;
    private int price;

    @Builder
    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public ItemEditor.ItemEditorBuilder toEditor() {
        return ItemEditor.builder()
                .itemName(name)
                .price(price);
    }

    public void edit(ItemEditor itemEditor) {
        name = itemEditor.getName();
        price = itemEditor.getPrice();
    }

}
