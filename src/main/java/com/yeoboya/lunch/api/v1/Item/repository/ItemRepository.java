package com.yeoboya.lunch.api.v1.Item.repository;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    Optional<Item> getItemByShopNameAndName(String shopName, String name);

    List<Item> getList(Pageable pageable);
}
