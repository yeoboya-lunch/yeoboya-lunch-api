package com.yeoboya.lunch.api.v1.order.domain;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

    //주문자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_BUY_MEMBER")
    private GroupOrder groupOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;      //주문 상품

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID", insertable = true)
    private Order order;

    private int orderPrice;  //주문 가격
    private int orderQuantity; //주문 수량

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, Order order, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrder(order);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setOrderQuantity(count);

        return orderItem;
    }

    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getOrderQuantity();
    }

    public void updateQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", buyPrice=" + orderPrice +
                ", count=" + orderQuantity +
                '}';
    }
}
