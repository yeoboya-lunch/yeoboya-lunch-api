package com.yeoboya.lunch.api.v1.event.reqeust;

import com.yeoboya.lunch.api.v1.event.domain.Banner;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class BannerRequest {

    @NotBlank(message = "Title is mandatory")
    private String title;  // 배너 제목

    @Min(value = 0, message = "Display order must be a positive number")
    private int displayOrder;  // 배너 이미지 순서

    @NotNull(message = "Start date is mandatory")
    private LocalDateTime startDate;  // 배너 표시 시작일자

    @NotNull(message = "End date is mandatory")
    private LocalDateTime endDate;  // 배너 표시 종료일자

    @NotNull(message = "Display location is mandatory")
    private Banner.DisplayLocation displayLocation;  // 메인페이지 / 마이페이지 / 둘다 구분
}
