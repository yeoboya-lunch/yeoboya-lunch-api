package com.yeoboya.lunch.api.v1.order.repository;


import com.yeoboya.lunch.api.v1.order.domain.GroupOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupOrderRepository extends JpaRepository<GroupOrder, Long>, GroupOrderRepositoryCustom {

}
