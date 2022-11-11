package com.yeoboya.lunch.api.v1.order.repository;

import com.yeoboya.lunch.api.v1.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

}
