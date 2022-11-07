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
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
//    private final OrderQueryRepositoryImpl orderQueryRepositoryImpl;
    private final UsersJpaRepository usersJpaRepository;
    private final ItemRepository itemRepository;


    @Override
    public OrderResponse order(OrderCreate orderCreate) {

        Member member = usersJpaRepository.findByEmail(orderCreate.getEmail()).
                orElseThrow(() -> new UsernameNotFoundException("Member not found - " + orderCreate.getEmail()));
        Item item = itemRepository.findById((long) orderCreate.getItemId()).orElseThrow(ItemNotFound::new);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderCreate.getOrderQuantity());
        Order order = Order.createOrder(member, orderItem);
        Order save = orderRepository.save(order);

        OrderResponse build = OrderResponse.builder().
                id(save.getId()).
                memberId(save.getMember().getName()).
                price(save.getTotalPrice()).
                build();
        return build;
    }

    @Override
    public void update(Long itemId, OrderEdit orderEdit) {

    }

    @Override
    public Optional<OrderService> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<OrderResponse> findItems(OrderSearch orderSearch) {

//        PageRequest pageRequest = PageRequest.of(0, 5);

        return orderRepository.findAll().stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }
}
