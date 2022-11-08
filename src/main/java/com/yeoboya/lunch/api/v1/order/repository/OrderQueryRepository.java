package com.yeoboya.lunch.api.v1.order.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderSearchCond;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.yeoboya.lunch.api.v1.Item.domain.QItem.item;
import static com.yeoboya.lunch.api.v1.order.domain.QOrder.order;
import static com.yeoboya.lunch.api.v1.order.domain.QOrderItem.orderItem;
import static com.yeoboya.lunch.config.security.domain.QMember.member;


@Repository
public class OrderQueryRepository {

    private final JPAQueryFactory query;

    public OrderQueryRepository(JPAQueryFactory query) {
        this.query = query;
    }


    public List<Order> findAllByQueryDsl(){
        return query.selectFrom(order).
                leftJoin(order.orderItems, orderItem)
                .fetchJoin()
                .leftJoin(order.member, member)
                .fetchJoin()
                .distinct()
                .fetch();
    }


    public List<Item> findAll(OrderSearchCond cond) {
        return query.select(item).
                from(item).
                where(maxPrice(cond.getMaxPrice()), likeItemName(cond.getItemName())).
                fetch();
    }

    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.name.like("%" + itemName + "%");
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
        }
        return null;
    }

}
