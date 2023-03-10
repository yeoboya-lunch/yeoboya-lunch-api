package com.yeoboya.lunch.api.v1.order.response;

import com.yeoboya.lunch.api.v1.order.domain.GroupOrder;
import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
public class GroupOrderRecruitmentResponse {

    @Builder
    public GroupOrderRecruitmentResponse(Long orderId, String orderMemberEmail, String orderMemberName) {
        this.orderId = orderId;
        this.orderMemberEmail = orderMemberEmail;
        this.orderMemberName = orderMemberName;
    }

    private final Long orderId;
    private final String orderMemberEmail;
    private final String orderMemberName;


    public static GroupOrderRecruitmentResponse from(GroupOrder groupOrder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 a HH:mm");
        return new GroupOrderRecruitmentResponse(
                groupOrder.getId(),
                groupOrder.getMember().getEmail(),
                groupOrder.getMember().getName()
        );
    }
}