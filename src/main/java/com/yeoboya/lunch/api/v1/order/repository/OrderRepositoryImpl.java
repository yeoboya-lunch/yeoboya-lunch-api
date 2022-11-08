package com.yeoboya.lunch.api.v1.order.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderSearch;
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

    public List<OrderItem> orderItemList(OrderSearch orderSearch){
        return query.selectFrom(orderItem)
                .leftJoin(orderItem.order, order)
                .fetchJoin()
                .where(maxPrice(orderSearch.getOrderPrice()))
                .distinct()
                .fetch();
    }

    @Override
    public List<Order> orderList(OrderSearch orderSearch){
        return query.selectFrom(order)
                .leftJoin(order.orderItems, orderItem)
                .fetchJoin()
                .leftJoin(order.member, member)
                .fetchJoin()
                .distinct()
                .limit(orderSearch.getSize())
                .offset(orderSearch.getOffset())
                .fetch();
    }


//    @Override
//    public BooleanExpression likeItemName(String itemName) {
//        if (StringUtils.hasText(itemName)) {
//            return item.name.like("%" + itemName + "%");
//        }
//        return null;
//    }

    @Override
    public BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return orderItem.orderPrice.goe(maxPrice);
        }
        return null;
    }

}
