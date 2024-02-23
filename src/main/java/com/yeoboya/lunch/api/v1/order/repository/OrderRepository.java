package com.yeoboya.lunch.api.v1.order.repository;

import com.yeoboya.lunch.api.v1.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

}
