package com.yeoboya.lunch.api.v1.event.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BANNER_ID")
    private Long id;

    @Column(nullable = false)
    private String title;  // 배너 제목

    @Column(nullable = false)
    private String imageUrl;  // 배너 이미지 URL

    @Column(nullable = false)
    private int displayOrder;  // 배너 이미지 순서

    @Column(nullable = false)
    private LocalDateTime startDate;  // 배너 표시 시작일자

    @Column(nullable = false)
    private LocalDateTime endDate;  // 배너 표시 종료일자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisplayLocation displayLocation;  // 메인페이지 / 마이페이지 / 둘다 구분

    @Builder
    public Banner(String title, String imageUrl, int displayOrder, LocalDateTime startDate, LocalDateTime endDate, DisplayLocation displayLocation) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
        this.startDate = startDate;
        this.endDate = endDate;
        this.displayLocation = displayLocation;
    }

    public enum DisplayLocation {
        MAIN_PAGE,
        MY_PAGE,
    }
}
