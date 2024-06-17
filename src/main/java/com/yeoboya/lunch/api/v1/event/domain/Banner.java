package com.yeoboya.lunch.api.v1.event.domain;

import com.yeoboya.lunch.api.v1.file.domain.BannerFile;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BANNER_ID")
    private Long id;

    @Column(nullable = false)
    private String title;  // 배너 제목

    @Column(nullable = false)
    private int displayOrder;  // 배너 이미지 순서

    @Column(nullable = false)
    private LocalDateTime startDate;  // 배너 표시 시작일자

    @Column(nullable = false)
    private LocalDateTime endDate;  // 배너 표시 종료일자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisplayLocation displayLocation;  // 메인페이지 / 마이페이지 / 둘다 구분

    @Builder.Default
    @OneToMany(mappedBy = "banner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BannerFile> bannerFiles = new ArrayList<>();


    // 정적 팩토리 메서드 추가
    public static Banner createBanner(String title, int displayOrder, LocalDateTime startDate, LocalDateTime endDate, DisplayLocation displayLocation, BannerFile bannerFiles) {
        Banner banner = new Banner();
        banner.setTitle(title);
        banner.setDisplayOrder(displayOrder);
        banner.setStartDate(startDate);
        banner.setEndDate(endDate);
        banner.setDisplayLocation(displayLocation);
        banner.setDisplayLocation(displayLocation);
        if (bannerFiles != null) {
            banner.addFile(bannerFiles);
        }
        return banner;
    }

    private void addFile(BannerFile bannerFile) {
        this.bannerFiles.add(bannerFile);
        if (bannerFile.getBanner() != this) {
            bannerFile.setBanner(this);
        }
    }
    public enum DisplayLocation {
        ALL_PAGE,
        MAIN_PAGE,
        MY_PAGE,
    }
}
