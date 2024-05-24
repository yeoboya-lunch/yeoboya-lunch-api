package com.yeoboya.lunch.api.v1.order.repository;

import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
