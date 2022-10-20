package com.yeoboya.guinGujik.api.jpa.repository;

import com.yeoboya.guinGujik.api.jpa.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
