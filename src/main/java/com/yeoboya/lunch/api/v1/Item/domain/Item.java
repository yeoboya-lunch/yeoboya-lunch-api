package com.yeoboya.lunch.api.v1.Item.domain;

import com.yeoboya.lunch.api.v1.Item.request.ItemEditor;
import com.yeoboya.lunch.api.v1.domain.BaseEntity;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "NAME_SHOP_ID_UNIQUE",
                        columnNames = {"name", "shop_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOP_ID")
    private Shop shop;

    @Builder
    public Item(Shop shop, String name, int price) {
        this.shop = shop;
        this.name = name;
        this.price = price;
    }

    public ItemEditor.ItemEditorBuilder toEditor() {
        return ItemEditor.builder().itemName(name).price(price);
    }

    public void edit(ItemEditor itemEditor) {
        name = itemEditor.getName();
        price = itemEditor.getPrice();
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + '}';
    }
}
