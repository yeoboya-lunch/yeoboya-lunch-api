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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
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

    @Deprecated
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

    private BooleanExpression likeItemName(String orderName) {
        if (StringUtils.hasText(orderName)) {
            return order.member.name.like("%" + orderName + "%");
        }
        return null;
    }


    //fixme 날짜 조회
    private BooleanExpression eqDate(Date startDate, Date endDate) {

        if (startDate == null && endDate == null) { // 오늘날짜 Order
            LocalDateTime currentStartDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
            LocalDateTime currentEndDate = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59,59));

            System.out.println("currentStartDate = " + currentStartDate);
            System.out.println("currentEndDate = " + currentEndDate);
//            return order.orderDate.between(currentStartDate, currentEndDate);
        }

        if (startDate != null && endDate != null) { // 시작~종료 order
            return order.orderDate.between(startDate, endDate);
        }

        return null;
    }

//    private BooleanExpression searchDateQuery(LocalDateTime startDate, LocalDateTime endDate) {
//        BooleanExpression searchQuery = null;
//        if (startDate == null && endDate == null){
//            LocalDateTime currentStartDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
//            LocalDateTime currentEndDate = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59,59));
//            //날짜데이터 없으면 하루 시작과 끝
//            searchQuery = order.orderDate.between(currentStartDate, currentEndDate);
//        } else if (startDate != null && endDate == null) {
//            LocalDateTime startDateTime = LocalDateTime.of(LocalDate.from(startDate),LocalTime.of(0,0,0));
//            //시작 날짜만 있으면 있으면 시작날부터 쭉 뒤로
//            searchQuery = order.orderDate.goe(startDateTime);
//        } else if (startDate == null) {
//            //시작날짜 없으면 해당하는 달 1일 부터 조회일 까지
//            LocalDate firstMonthDate = LocalDate.from(endDate.with(TemporalAdjusters.firstDayOfMonth()));
//            LocalDateTime startDateTime = LocalDateTime.of(firstMonthDate,LocalTime.of(0,0,0));
//            searchQuery = order.orderDate.between(startDateTime,endDate);
//        } else {
//            searchQuery = order.orderDate.between(startDate,endDate);
//        }
//        return searchQuery;
//    }



}
