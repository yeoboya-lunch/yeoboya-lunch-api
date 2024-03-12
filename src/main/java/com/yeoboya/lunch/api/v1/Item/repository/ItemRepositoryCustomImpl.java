package com.yeoboya.lunch.api.v1.Item.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.Item.response.ItemResponse;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.response.ShopResponse;
import com.yeoboya.lunch.config.persistence.QueryDslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yeoboya.lunch.api.v1.Item.domain.QItem.item;
import static com.yeoboya.lunch.api.v1.shop.domain.QShop.shop;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    public Map<String, List<ItemResponse>> getItemsByShop(String shopName, Pageable pageable) {

        QueryResults<Item> results = jpaQueryFactory.selectFrom(item)
                .join(item.shop, shop)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(
                        shop.name.eq(shopName)
                )
                .orderBy(this.sort(pageable).toArray(OrderSpecifier[]::new))
                .fetchResults();

        List<Item> items = results.getResults();

        Map<Shop, List<Item>> map = items.stream()
                .collect(Collectors.groupingBy(Item::getShop));

        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ShopResponse.from(entry.getKey()).getShopName(),
                        entry -> entry.getValue().stream()
                                .map(ItemResponse::from)
                                .collect(Collectors.toList())
                ));

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
