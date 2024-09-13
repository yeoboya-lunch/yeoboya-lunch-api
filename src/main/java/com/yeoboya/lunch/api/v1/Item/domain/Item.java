package com.yeoboya.lunch.api.v1.Item.domain;

import com.yeoboya.lunch.api.v1.Item.request.ItemEditor;
import com.yeoboya.lunch.api.v1.common.domain.BaseEntity;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_NAME_SHOP",
                        columnNames = {"name", "shop_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // 관계 필드에 nullable을 명시적으로 지정
    @JoinColumn(name = "SHOP_ID", nullable = false)
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
