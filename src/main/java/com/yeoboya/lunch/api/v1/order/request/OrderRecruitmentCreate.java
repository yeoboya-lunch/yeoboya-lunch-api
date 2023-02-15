package com.yeoboya.lunch.api.v1.order.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class OrderRecruitmentCreate {

    @NotBlank(message = "주문자를 입력해주세요.")
    private String email;

    @NotNull(message = "상점을 입력해주세요.")
    private String shopName;

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    private int deliveryFee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "주문 마감 시간을 입력해주세요.")
    private Timestamp lastOrderTime;

    @NotNull(message = "메모를 입력해주세요.")
    private String memo;


    @Builder
    public OrderRecruitmentCreate(String email, String shopName, String title, int deliveryFee, Timestamp lastOrderTime, String memo) {
        this.email = email;
        this.shopName = shopName;
        this.title = title;
        this.deliveryFee = deliveryFee;
        this.lastOrderTime = lastOrderTime;
        this.memo = memo;
    }
}