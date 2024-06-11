package com.yeoboya.lunch.api.v1.event.controller;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.event.domain.Banner;
import com.yeoboya.lunch.api.v1.event.reqeust.BannerRequest;
import com.yeoboya.lunch.api.v1.event.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<Response.Body> getBanners(@RequestParam(required = false) Banner.DisplayLocation displayLocation) {
        return bannerService.getBanners(Optional.ofNullable(displayLocation));
    }

    @PostMapping
    public ResponseEntity<Response.Body> createBanner(@Valid @RequestBody BannerRequest bannerRequest) {
        return bannerService.saveBanner(bannerRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response.Body> deleteBanner(@PathVariable Long id) {
        return bannerService.deleteBanner(id);
    }
}
