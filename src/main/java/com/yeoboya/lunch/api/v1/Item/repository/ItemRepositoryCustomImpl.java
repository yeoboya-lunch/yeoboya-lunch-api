package com.yeoboya.lunch.api.v1.Item.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.config.persistence.QueryDslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.yeoboya.lunch.api.v1.Item.domain.QItem.item;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Item> getList(Pageable pageable) {
        return jpaQueryFactory.selectFrom(item)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(this.sort(pageable).toArray(OrderSpecifier[]::new))
                .fetch();
    }


    private List<OrderSpecifier> sort(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "name":
                        OrderSpecifier<?> itemName = QueryDslUtil.getSortedColumn(direction, item.name, "name");
                        ORDERS.add(itemName);
                        break;
                    case "price":
                        OrderSpecifier<?> itemPrice = QueryDslUtil.getSortedColumn(direction, item.price, "price");
                        ORDERS.add(itemPrice);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }

}
