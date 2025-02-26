package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.config.security.domain.Resources;
import com.yeoboya.lunch.config.security.domain.Role;
import com.yeoboya.lunch.config.security.repository.ResourcesRepository;
import com.yeoboya.lunch.config.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicResourceService {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final ResourcesRepository resourcesRepository;
    private final RoleRepository roleRepository;


    /**
     * Spring Security 리소스(자원) 목록을 자동으로 스캔하여 DB에 반영한다.
     * 새로운 리소스가 발견되면 자동으로 추가된다.
     */
    @Transactional
    public void syncResources() {
        log.warn("동적 리소스 동기화 시작...");

        // 현재 DB에 저장된 리소스 목록 조회
        Set<String> existingResources = resourcesRepository.findAll()
                .stream()
                .map(Resources::getResourceName)
                .collect(Collectors.toSet());

        // 리소스 매핑을 로드
        Set<String> scannedResources = requestMappingHandlerMapping.getHandlerMethods().keySet()
                .stream()
                .flatMap(info -> info.getDirectPaths().stream())
                .collect(Collectors.toSet());

        // 확인용 출력
        requestMappingHandlerMapping.getHandlerMethods().keySet()
                .forEach(info -> log.debug("🔍 getPatternsCondition: {}, getDirectPaths: {}",
                        info.getPatternsCondition(), info.getDirectPaths()));


        // 기존 개별 리소스 → 그룹화된 패턴으로 정리 (기존 데이터가 없을 경우만 추가)
        Map<String, Map.Entry<String, Integer>> patternMappings = new HashMap<>();
        patternMappings.put("/role", new AbstractMap.SimpleEntry<>("/role/**", 100));
        patternMappings.put("/resource", new AbstractMap.SimpleEntry<>("/resource/**", 100));

        patternMappings.put("/user", new AbstractMap.SimpleEntry<>("/user/**", 200));
        patternMappings.put("/member", new AbstractMap.SimpleEntry<>("/member/**", 200));

        patternMappings.put("/shop", new AbstractMap.SimpleEntry<>("/shop/**", 300));
        patternMappings.put("/item", new AbstractMap.SimpleEntry<>("/item/**", 300));
        patternMappings.put("/order", new AbstractMap.SimpleEntry<>("/order/**", 300));
        patternMappings.put("/reviews", new AbstractMap.SimpleEntry<>("/reviews/**", 300));

        patternMappings.put("/board", new AbstractMap.SimpleEntry<>("/board/**", 400));
        patternMappings.put("/file", new AbstractMap.SimpleEntry<>("/file/**", 400));
        patternMappings.put("/support", new AbstractMap.SimpleEntry<>("/support/**", 400));
        patternMappings.put("/banners", new AbstractMap.SimpleEntry<>("/banners/**", 400));

        // 그룹화된 리소스 저장
        Map<String, Integer> groupedResources = new HashMap<>();
        for (String resource : scannedResources) {
            boolean isGrouped = false;
            for (Map.Entry<String, Map.Entry<String, Integer>> entry : patternMappings.entrySet()) {
                if (resource.startsWith(entry.getKey())) {
                    groupedResources.put(entry.getValue().getKey(), entry.getValue().getValue()); // 패턴과 orderNum 저장
                    isGrouped = true;
                    break;
                }
            }
            if (!isGrouped) {
                groupedResources.put(resource, 999); // 기본 orderNum 값
            }
        }

        // 확인용 출력
        groupedResources.forEach((key, value) -> log.debug("Resource: {} OrderNum: {} ", key, value));

        // 새롭게 추가해야 할 리소스 찾기 (중복 검사)
        List<Resources> newResources = groupedResources.entrySet().stream()
                .filter(entry -> !existingResources.contains(entry.getKey()))
                .map(entry -> Resources.builder()
                        .resourceName(entry.getKey())
                        .resourceType("url")
                        .orderNum(entry.getValue())
                        .httpMethod(null)
                        .build()
                )
                .collect(Collectors.toList());;

        if (!newResources.isEmpty()) {
            resourcesRepository.saveAll(newResources);
            log.warn("{}개의 새로운 리소스 추가 완료", newResources.size());
        } else {
            log.warn("새로운 리소스 없음");
        }
    }
}


