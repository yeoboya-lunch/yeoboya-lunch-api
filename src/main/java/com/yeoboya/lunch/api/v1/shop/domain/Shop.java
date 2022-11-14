package com.yeoboya.lunch.api.v1.shop.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yeoboya.lunch.api.v1.Item.domain.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SHOP_ID", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Item> items = new ArrayList<>();

    @Builder
    public Shop(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name +
                ", items=" + items +
                '}';
    }
}