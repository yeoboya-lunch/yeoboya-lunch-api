package com.yeoboya.lunch.api.v1.order.service;

import com.yeoboya.lunch.api.v1.Item.domain.Item;
import com.yeoboya.lunch.api.v1.Item.repository.ItemRepository;
import com.yeoboya.lunch.api.v1.exception.ItemNotFound;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.domain.OrderItem;
import com.yeoboya.lunch.api.v1.order.repository.OrderRepository;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderCreate;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderEdit;
import com.yeoboya.lunch.api.v1.order.reqeust.OrderSearch;
import com.yeoboya.lunch.api.v1.order.response.OrderItemResponse;
import com.yeoboya.lunch.api.v1.order.response.OrderResponse;
import com.yeoboya.lunch.config.security.domain.Member;
import com.yeoboya.lunch.config.security.repository.UsersJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Item item = itemRepository.findById((long) orderCreate.getItemId()).orElseThrow(ItemNotFound::new);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderCreate.getOrderQuantity());
        Order order = Order.createOrder(member, orderItem);
        Order save = orderRepository.save(order);

        return OrderResponse.builder().
                id(save.getId()).
                email(save.getMember().getEmail()).
                price(save.getTotalPrice()).
                build();
    }

    public void update(Long itemId, OrderEdit orderEdit) {

    }

    public Optional<OrderService> findById(Long id) {
        return Optional.empty();
    }

    public List<OrderResponse> orderList(OrderSearch orderSearch) {
        return orderRepository.orderList(orderSearch).stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public List<OrderItemResponse> orderItemList(OrderSearch search) {
        return orderRepository.orderItemList(search).stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }
}
