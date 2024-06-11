package com.yeoboya.lunch.api.v1.event.service;

import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.event.domain.Banner;
import com.yeoboya.lunch.api.v1.event.repository.BannerRepository;
import com.yeoboya.lunch.api.v1.event.reqeust.BannerRequest;
import com.yeoboya.lunch.api.v1.event.response.BannerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final Response response;

    public ResponseEntity<Response.Body> getBanners(Optional<Banner.DisplayLocation> displayLocation) {

        LocalDateTime now = LocalDateTime.now();
        List<Banner> banners;
        if (displayLocation.isPresent()) {
            banners = bannerRepository.findAllByDisplayLocationAndDateRange(displayLocation.get(), now);
        } else {
            banners = bannerRepository.findAllByDateRange(now);
        }

        List<BannerResponse> bannerResponses = banners.stream()
                .map(banner -> new BannerResponse(
                        banner.getId(),
                        banner.getTitle(),
                        banner.getImageUrl(),
                        banner.getDisplayOrder(),
                        banner.getStartDate(),
                        banner.getEndDate(),
                        banner.getDisplayLocation().name()
                ))
                .collect(Collectors.toList());

        return response.success(Code.SEARCH_SUCCESS, bannerResponses);
    }

    public ResponseEntity<Response.Body> saveBanner(BannerRequest bannerRequest) {
        Banner banner = Banner.builder()
                .title(bannerRequest.getTitle())
                .imageUrl(bannerRequest.getImageUrl())
                .displayOrder(bannerRequest.getDisplayOrder())
                .startDate(bannerRequest.getStartDate())
                .endDate(bannerRequest.getEndDate())
                .displayLocation(bannerRequest.getDisplayLocation())
                .build();
        bannerRepository.save(banner);
        return response.success(Code.SAVE_SUCCESS);
    }

    public ResponseEntity<Response.Body> deleteBanner(Long id) {
        bannerRepository.deleteById(id);
        return response.success(Code.DELETE_SUCCESS);
    }
}
