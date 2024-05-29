package com.yeoboya.lunch.api.v1.order.domain;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_ORDER_MEMBER",
                        columnNames = {"ORDER_ID", "MEMBER_ID"}
                )
        }
)
public class GroupOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_ORDER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "groupOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Date orderDate;     //주문시간

    //연관관계 편의 메소드
    public static GroupOrder createGroupOrder(Order order, Member member, List<OrderItem> orderItems) {
        GroupOrder groupOrder = new GroupOrder();
        groupOrder.setOrder(order);
        groupOrder.setMember(member);
        for (OrderItem orderItem : orderItems) {
            groupOrder.addOrderItem(orderItem, order);
        }
        groupOrder.setOrderDate(new Date());
        return groupOrder;
    }


    public void addOrderItem(OrderItem orderItem, Order order) {
        this.orderItems.add(orderItem);
        orderItem.setGroupOrder(this);
        orderItem.setOrder(order);
    }

    @Override
    public String toString() {
        return "GroupOrder{" +
                "id=" + id +
                ", order=" + order +
                ", member=" + member +
                ", orderItems=" + orderItems +
                ", orderDate=" + orderDate +
                '}';
    }
}
