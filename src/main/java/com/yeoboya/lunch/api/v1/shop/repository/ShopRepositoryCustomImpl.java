package com.yeoboya.lunch.api.v1.shop.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.request.ShopSearch;
import com.yeoboya.lunch.config.persistence.QueryDslUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.yeoboya.lunch.api.v1.Item.domain.QItem.item;
import static com.yeoboya.lunch.api.v1.shop.domain.QShop.shop;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class ShopRepositoryCustomImpl implements ShopRepositoryCustom {

    private final JPAQueryFactory query;

    public ShopRepositoryCustomImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public List<Shop> shopItem(ShopSearch search, Pageable pageable) {

        return query.selectFrom(shop)
                .leftJoin(shop.items, item)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(this.sort(pageable).stream().toArray(OrderSpecifier[]::new))
                .where(this.likeItemName(search.getShopName()))
                .distinct()
                .fetch();
    }


    private BooleanExpression likeItemName(String shopName) {
        if (StringUtils.hasText(shopName)) {
            return shop.name.like("%" + shopName + "%");
        }
        return null;
    }



    //fixme
    private List<OrderSpecifier> sort(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "id":
                        OrderSpecifier<?> shopId = QueryDslUtil.getSortedColumn(direction, shop.id, "id");
                        ORDERS.add(shopId);
                        break;
                    case "name":
                        OrderSpecifier<?> shopName = QueryDslUtil.getSortedColumn(direction, shop.name, "name");
                        ORDERS.add(shopName);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }
}
