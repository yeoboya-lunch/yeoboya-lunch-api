package com.yeoboya.lunch.api.v1.order.domain;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.request.OrderRecruitmentCreate;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;  //주문 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOP_ID")
    private Shop shop;  // 주문 상점

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();    //주문아이템 //todo orderMember

    private String title;         //제목

    private int deliveryFee;      //배달비

    private Date lastOrderTime;   //L.O

    private Date orderDate;     //주문시간

    private String memo;    //메모

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태

    //연관관계 편의 메소드
    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(new Date());
        return order;
    }

    //연관관계 편의 메소드
    public static Order recruit(Member member, Shop shop, OrderRecruitmentCreate create) {
        Order order = new Order();
        order.setMember(member);
        order.setShop(shop);
        order.setStatus(OrderStatus.START);
        order.setOrderDate(new Date());
        order.setTitle(create.getTitle());
        order.setDeliveryFee(create.getDeliveryFee());
        order.setLastOrderTime(create.getLastOrderTime());
        order.setMemo(create.getMemo());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", orderDate=" + orderDate + ", status=" + status + '}';
    }
}
