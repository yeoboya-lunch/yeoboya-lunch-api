package com.yeoboya.lunch.api.v1.order.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.yeoboya.lunch.api.v1.member.domain.QMember.member;
import static com.yeoboya.lunch.api.v1.order.domain.QOrder.order;
import static com.yeoboya.lunch.api.v1.order.domain.QOrderItem.orderItem;

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
                        isStatus(orderSearch.getOrderStatus()),
                        likeItemName(orderSearch.getOrderName()),
                        eqDate(orderSearch.getStartDate(), orderSearch.getEndDate())
                )
                .distinct()
                .fetch();
    }

    @Override
    public List<OrderItem> orderItems(Long orderID) {
        return null;
    }

    private BooleanExpression isStatus(OrderStatus status){
        return status != null ? order.status.eq(status) : null;
    }

    private BooleanExpression likeItemName(String orderName) {
        if (StringUtils.hasText(orderName)) {
            return order.member.name.like("%" + orderName + "%");
        }
        return null;
    }

    //fixme 날짜 조회
    private BooleanExpression eqDate(LocalDate startDate, LocalDate endDate) {

        System.out.println("startDate = " + startDate + ", endDate = " + endDate);
        if (startDate == null && endDate == null) { // 오늘날짜 Order
            Timestamp start = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0)));
            Timestamp end = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)));
            return order.orderDate.between(start,end);
        }

        if (startDate != null && endDate != null) { // 시작~종료 order
            Date start = Date.valueOf(startDate);
            Date end = Date.valueOf(endDate);
            return order.orderDate.between(start, end);
        }
        return null;
    }

}
