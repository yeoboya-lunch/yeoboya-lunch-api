package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.config.annotation.Reload;
import com.yeoboya.lunch.config.security.reqeust.ResourcesRequest;
import com.yeoboya.lunch.config.security.reqeust.TokenIgnoreUrlRequest;
import com.yeoboya.lunch.config.security.service.ResourcesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourcesService resourcesService;

    /**
     * 리소스추가
     */
    @Reload
    @PostMapping("/add")
    public ResponseEntity<Response.Body> addResource(@RequestBody ResourcesRequest resourcesRequest) {
        return resourcesService.addResources(resourcesRequest);
    }

    /**
     * 리소스조회
     */
    @GetMapping
    public ResponseEntity<Response.Body> resources(Pageable pageable){
        return resourcesService.fetchAllResources(pageable);
    }

    /**
     * 리소스삭제
     */
    @Reload
    @DeleteMapping
    public ResponseEntity<Response.Body> deleteResource(){
        return null;
    }

    /**
     * JWT 토큰 (url) 관리
     */
    @PostMapping("/token-ignore-url")
    public  ResponseEntity<Response.Body> tokenIgnoreUrl(@RequestBody TokenIgnoreUrlRequest tokenIgnoreUrlRequest){
        return resourcesService.tokenIgnoreUrl(tokenIgnoreUrlRequest);
    }
}

