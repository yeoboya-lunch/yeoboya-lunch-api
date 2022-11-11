package com.yeoboya.lunch.api.v1.order.service;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.Item.repository.ItemRepository;
import com.yeoboya.lunch.api.v1.exception.ItemNotFound;
import com.yeoboya.lunch.api.v1.exception.OrderNotFound;
import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import com.yeoboya.lunch.api.v1.order.repository.OrderRepository;
import com.yeoboya.lunch.api.v1.order.request.OrderCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderEdit;
import com.yeoboya.lunch.api.v1.order.request.OrderItemCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
import com.yeoboya.lunch.api.v1.order.response.OrderItemResponse;
import com.yeoboya.lunch.api.v1.order.response.OrderResponse;
import com.yeoboya.lunch.config.security.domain.Member;
import com.yeoboya.lunch.config.security.repository.UsersJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UsersJpaRepository usersJpaRepository;
    private final ItemRepository itemRepository;


    public OrderResponse order(OrderCreate orderCreate) {

        Member member = usersJpaRepository.findByEmail(orderCreate.getEmail()).
                orElseThrow(() -> new UsernameNotFoundException("Member not found - " + orderCreate.getEmail()));

        List<OrderItemCreate> orderItemCreates = orderCreate.getOrderItems();

        Item item;
        List<OrderItem> orderItems = new ArrayList<>();
        Order order;

        for (OrderItemCreate orderItemCreate : orderItemCreates){
            item = itemRepository.getItemByShopNameAndName(orderCreate.getShopName(), orderItemCreate.getItemName()).orElseThrow(ItemNotFound::new);
            orderItems.add(OrderItem.createOrderItem(item, item.getPrice(), orderItemCreate.getOrderQuantity()));
        }
        order = Order.createOrder(member, orderItems);
        Order save = orderRepository.save(order);

        return OrderResponse.builder().
                id(save.getId()).
                orderName(save.getMember().getName()).
                totalPrice(save.getTotalPrice()).
                order(order).
                build();
    }

    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        order.setStatus(OrderStatus.CANCEL);
    }

    public List<OrderResponse> orderList(OrderSearch orderSearch, Pageable pageable) {
        return orderRepository.orderList(orderSearch, pageable).stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateOrder(Long orderId, OrderEdit edit) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);

        List<OrderItem> orderItems = orderRepository.orderItems(orderId);

    }

    @Deprecated
    public List<OrderItemResponse> orderItemList(OrderSearch search, Pageable pageable) {
        return orderRepository.orderItemList(search, pageable).stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }


}
