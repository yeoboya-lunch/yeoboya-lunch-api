package com.yeoboya.lunch.api.v1.order.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yeoboya.lunch.api.v1.order.domain.QOrder.order;
import static com.yeoboya.lunch.api.v1.order.domain.QOrderItem.orderItem;
import static com.yeoboya.lunch.config.security.domain.QMember.member;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory query;

    public OrderRepositoryImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public List<Order> orderList(OrderSearch orderSearch, Pageable pageable){
        return query.selectFrom(order)
                .leftJoin(order.orderItems, orderItem)
                .leftJoin(order.member, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.id.desc())
                .where(
                        isStatus(orderSearch.getOrderStatus())
                )
                .distinct()
                .fetch();
    }

    @Override
    public List<OrderItem> orderItemList(OrderSearch orderSearch, Pageable pageable){
        return query.selectFrom(orderItem)
                .leftJoin(orderItem.order, order).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    private BooleanExpression isStatus(OrderStatus status){
        return status != null ? order.status.eq(status) : null;
    }


    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return orderItem.orderPrice.goe(maxPrice);
        }
        return null;
    }

}
