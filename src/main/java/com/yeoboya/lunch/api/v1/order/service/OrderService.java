package com.yeoboya.lunch.api.v1.order.service;

import com.yeoboya.lunch.api.v1.Item.repository.ItemRepository;
import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.domain.Order;
import com.yeoboya.lunch.api.v1.order.repository.OrderRepository;
import com.yeoboya.lunch.api.v1.order.request.OrderRecruitmentCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderSearch;
import com.yeoboya.lunch.api.v1.order.response.OrderDetailResponse;
import com.yeoboya.lunch.api.v1.order.response.OrderRecruitmentResponse;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.repository.ShopRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, ShopRepository shopRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.shopRepository = shopRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }


    public OrderDetailResponse lunchOrderRecruitWrite(OrderRecruitmentCreate orderRecruitmentCreate) {
        Member member = memberRepository.findByEmail(orderRecruitmentCreate.getEmail()).
                orElseThrow(() -> new EntityNotFoundException("Member not found - " + orderRecruitmentCreate.getEmail()));

        Shop shop = shopRepository.findByName(orderRecruitmentCreate.getShopName()).
                orElseThrow(() -> new EntityNotFoundException("Shop not fount - " + orderRecruitmentCreate.getShopName()));

        Order order = Order.recruit(member, shop, orderRecruitmentCreate);
        Order save = orderRepository.save(order);

        return null;
    }

//    public OrderResponse order(OrderRecruitmentCreate orderRecruitmentCreate) {
//
//        Member member = memberRepository.findByEmail(orderRecruitmentCreate.getEmail()).
//                orElseThrow(() -> new EntityNotFoundException("Member not found - " + orderRecruitmentCreate.getEmail()));
//
//        List<OrderItemCreate> orderItemCreates = orderCreate.getOrderItems();
//
//        Item item;
//        List<OrderItem> orderItems = new ArrayList<>();
//
//        for (OrderItemCreate orderItemCreate : orderItemCreates) {
//            item = itemRepository.getItemByShopNameAndName(orderRecruitmentCreate.getShopName(), orderItemCreate.getItemName())
//                    .orElseThrow(() -> new EntityNotFoundException("Item not found - " + orderItemCreate.getItemName()));
//            orderItems.add(OrderItem.createOrderItem(item, item.getPrice(), orderItemCreate.getOrderQuantity()));
//        }
//
//        Order order = Order.createOrder(member, orderItems);
//        Order save = orderRepository.save(order);
//
//        return OrderResponse.builder()
//                .orderStatus(save.getStatus())
//                .orderName(save.getMember().getName())
//                .totalPrice(save.getTotalPrice())
//                .orderItems(order)
//                .build();
//    }


    public Map<String, Object> recruits(OrderSearch search, Pageable pageable) {
        Slice<Order> orders = orderRepository.orderRecruits(search, pageable);
        List<OrderRecruitmentResponse> orderRecruitmentResponses = orders.getContent().stream()
                .map(OrderRecruitmentResponse::from)
                .collect(Collectors.toList());

        return Map.of(
                "list", orderRecruitmentResponses,
                "isFirst", orders.isFirst(),
                "isLast", orders.isLast(),
                "hasNext", orders.hasNext(),
                "hasPrevious", orders.hasPrevious(),
                "pageNo", orders.getNumber() + 1
        );
    }


    public Map<String, Object> lunchRecruitByOrderId(Long orderNo) {
        Order order = orderRepository.findById(orderNo).orElseThrow(() -> new EntityNotFoundException("Order not found - " + orderNo));
        OrderDetailResponse orderInfo = OrderDetailResponse.orderInfo(order);
//        OrderDetailResponse orderMemberInfo = OrderDetailResponse.orderMemberInfo(order);


        return Map.of("order", orderInfo,
                "orderMember", "");
    }

//    public List<OrderDetailResponse> orderList(OrderSearch orderSearch, Pageable pageable) {
//        return orderRepository.orderList(orderSearch, pageable).stream()
//                .map(OrderDetailResponse::from)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found - " + orderId));
        order.setStatus(OrderStatus.CANCEL);
    }


}