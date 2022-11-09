package com.yeoboya.lunch.api.v1.Item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.Item.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.yeoboya.lunch.api.v1.Item.domain.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Item> getList(Pageable pageable) {
        return jpaQueryFactory.selectFrom(item)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(item.id.desc())
                .fetch();
    }

}
