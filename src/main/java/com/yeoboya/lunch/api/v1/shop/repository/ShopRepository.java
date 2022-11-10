package com.yeoboya.lunch.api.v1.shop.repository;

import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long>, ShopRepositoryCustom {

    Optional<Shop> findByName(String name);

}
