package com.yeoboya.lunch.api.v1.order.repository;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByOrderAndItemAndGroupOrderMember(Order order, Item item, Member member);

    List<OrderItem> findByOrderAndGroupOrderMember(Order order, Member member);
}
