package com.yeoboya.lunch.config.security.factory;

import com.yeoboya.lunch.config.security.service.SecurityResourceService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Spring Security에서 URL 기반 접근 제어를 위한 매핑 정보를 생성하는 FactoryBean
 */
@Slf4j
public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    @Setter
    private SecurityResourceService securityResourceService;

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourcesMap;

    /**
     * 초기화 메서드 (DB에서 리소스-권한 매핑 정보를 로드)
     */
    public void init() {
        log.info("Initializing URL Resources Map...");
        resourcesMap = securityResourceService.getResourceList();

        if (resourcesMap == null || resourcesMap.isEmpty()) {
            log.error("Failed to load resources map, initializing with an empty map!");
            resourcesMap = new LinkedHashMap<>();
        }
    }

    /**
     * FactoryBean이 관리할 객체 반환
     * @return URL과 권한 매핑 정보
     */
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() {
        if (resourcesMap == null) {
            init();
        }
        return resourcesMap;
    }

    /**
     * FactoryBean이 반환할 객체 타입 지정
     */
    public Class<LinkedHashMap> getObjectType() {
        return LinkedHashMap.class;
    }

    /**
     * 이 FactoryBean이 싱글턴 객체를 반환하는지 여부
     */
    public boolean isSingleton() {
        return true;
    }
}