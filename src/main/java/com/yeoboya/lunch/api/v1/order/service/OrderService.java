package com.yeoboya.lunch.api.v1.order.service;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.Item.repository.ItemRepository;
import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import com.yeoboya.lunch.api.v1.order.repository.OrderRepository;
import com.yeoboya.lunch.api.v1.order.request.OrderCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderItemCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
import com.yeoboya.lunch.api.v1.order.response.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

    public OrderResponse order(OrderCreate orderCreate) {

        Member member = memberRepository.findByEmail(orderCreate.getEmail()).
                orElseThrow(()->new EntityNotFoundException("Member not found - " + orderCreate.getEmail()));

        List<OrderItemCreate> orderItemCreates = orderCreate.getOrderItems();

        Item item;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemCreate orderItemCreate : orderItemCreates){
            item = itemRepository.getItemByShopNameAndName(orderCreate.getShopName(), orderItemCreate.getItemName())
                    .orElseThrow(()->new EntityNotFoundException("Item not found - " + orderItemCreate.getItemName()));
            orderItems.add(OrderItem.createOrderItem(item, item.getPrice(), orderItemCreate.getOrderQuantity()));
        }

        Order order = Order.createOrder(member, orderItems);
        Order save = orderRepository.save(order);

        return OrderResponse.builder()
                .orderStatus(save.getStatus())
                .orderName(save.getMember().getName())
                .totalPrice(save.getTotalPrice())
                .orderItems(order)
                .build();
    }

    public List<OrderResponse> orderList(OrderSearch orderSearch, Pageable pageable) {
        return orderRepository.orderList(orderSearch, pageable).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("Order not found - " + orderId));
        order.setStatus(OrderStatus.CANCEL);
    }

}