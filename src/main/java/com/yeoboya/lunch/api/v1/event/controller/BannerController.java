package com.yeoboya.lunch.api.v1.event.controller;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.event.controller.specification.BannerApi;
import com.yeoboya.lunch.api.v1.event.domain.Banner;
import com.yeoboya.lunch.api.v1.event.reqeust.BannerRequest;
import com.yeoboya.lunch.api.v1.event.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class BannerController  {

    private final BannerService bannerService;

    @PostMapping
    public ResponseEntity<Response.Body> createBanner(@RequestPart(value = "file", required = false) MultipartFile file, @RequestPart("bannerRequest") @Valid BannerRequest bannerRequest) {
        return bannerService.saveBanner(file, bannerRequest);
    }

    @GetMapping
    public ResponseEntity<Response.Body> getBanners(@RequestParam(required = false) Banner.DisplayLocation displayLocation) {
        return bannerService.getBanners(Optional.ofNullable(displayLocation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response.Body> deleteBanner(@PathVariable Long id) {
        return bannerService.deleteBanner(id);
    }
}
