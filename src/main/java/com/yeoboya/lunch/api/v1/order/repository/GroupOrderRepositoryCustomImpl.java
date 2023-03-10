package com.yeoboya.lunch.api.v1.order.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.order.domain.GroupOrder;
import com.yeoboya.lunch.api.v1.order.request.GroupOrderSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.yeoboya.lunch.api.v1.order.domain.QGroupOrder.groupOrder;

@Repository
public class GroupOrderRepositoryCustomImpl implements GroupOrderRepositoryCustom {

    private final JPAQueryFactory query;

    public GroupOrderRepositoryCustomImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public Slice<GroupOrder> purchaseRecruits(GroupOrderSearch orderSearch, Pageable pageable) {
        List<GroupOrder> content = query.selectFrom(groupOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(groupOrder.id.desc())
                .where(
                        eqEmail(orderSearch.getOrderEmail())
                )
                .distinct()
                .fetch();
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }



    private BooleanExpression eqEmail(String orderEmail) {
        if (StringUtils.hasText(orderEmail)) {
            return groupOrder.member.email.eq(orderEmail);
        }
        return null;
    }


    // default 오늘 하루 주문만 조회
    private BooleanExpression eqDate(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            Date start = Date.valueOf(startDate);
            Date end = Date.valueOf(endDate.plusDays(1));
            return groupOrder.orderDate.between(start, end);
        }

        if (startDate != null){
            Date start = Date.valueOf(startDate);
            Date end = Date.valueOf(startDate.plusDays(1));
            return groupOrder.orderDate.between(start, end);
        }

        if (endDate != null){
            Date start = Date.valueOf(endDate);
            Date end = Date.valueOf(endDate.plusDays(1));
            return groupOrder.orderDate.between(start, end);
        }

        Timestamp start = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0)));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)));
        return groupOrder.orderDate.between(start,end);
    }

}
