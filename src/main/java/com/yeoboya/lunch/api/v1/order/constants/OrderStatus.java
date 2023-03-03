package com.yeoboya.lunch.api.v1.order.constants;

public enum OrderStatus {
    ORDER_START(0, "모집시작"),
    END(1, "모집종료"),
    ORDER_SUCCESS(2, "주문완료"),
    CANCEL(3, "주문취소");

    private final int key;
    private final String title;

    OrderStatus(int key, String title) {
        this.key = key;
        this.title = title;
    }

    public int getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }
}
