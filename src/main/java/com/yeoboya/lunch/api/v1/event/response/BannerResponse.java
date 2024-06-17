package com.yeoboya.lunch.api.v1.event.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BannerResponse {

    private Long id;  // 배너 ID
    private String title;  // 배너 제목
    private int displayOrder;  // 배너 이미지 순서
    private LocalDateTime startDate;  // 배너 표시 시작일자
    private LocalDateTime endDate;  // 배너 표시 종료일자
    private String displayLocation;  // 메인페이지 / 마이페이지 / 둘다 구분
    private List<BannerFileResponse> bannerFiles; // 배너 파일 목록

    public BannerResponse(Long id, String title, int displayOrder, LocalDateTime startDate, LocalDateTime endDate, String displayLocation, List<BannerFileResponse> bannerFiles) {
        this.id = id;
        this.title = title;
        this.displayOrder = displayOrder;
        this.startDate = startDate;
        this.endDate = endDate;
        this.displayLocation = displayLocation;
        this.bannerFiles = bannerFiles;
    }
}
