package com.yeoboya.lunch.api.v1.event.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BannerResponse {

    private Long id;  // 배너 ID
    private String title;  // 배너 제목
    private String imageUrl;  // 배너 이미지 URL
    private int displayOrder;  // 배너 이미지 순서
    private LocalDateTime startDate;  // 배너 표시 시작일자
    private LocalDateTime endDate;  // 배너 표시 종료일자
    private String displayLocation;  // 메인페이지 / 마이페이지 / 둘다 구분

    public BannerResponse(Long id, String title, String imageUrl, int displayOrder, LocalDateTime startDate, LocalDateTime endDate, String displayLocation) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
        this.startDate = startDate;
        this.endDate = endDate;
        this.displayLocation = displayLocation;
    }
}
