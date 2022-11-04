package com.yeoboya.lunch.api.v1.Item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.Item.request.ItemSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.yeoboya.lunch.api.v1.Item.domain.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Item> getList(ItemSearch itemSearch) {
        return jpaQueryFactory.selectFrom(item)
                .limit(itemSearch.getSize())
                .offset(itemSearch.getOffset())
                .orderBy(item.id.desc())
                .fetch();
    }

}
