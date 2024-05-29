package com.yeoboya.lunch.config.security.service;


import com.yeoboya.lunch.config.security.domain.AccessIp;
import com.yeoboya.lunch.config.security.domain.Resources;
import com.yeoboya.lunch.config.security.repository.AccessIpRepository;
import com.yeoboya.lunch.config.security.repository.ResourcesRepository;
import com.yeoboya.lunch.config.security.repository.TokenIgnoreUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
public class SecurityResourceService {

    private final ResourcesRepository resourcesRepository;
    private final AccessIpRepository accessIpRepository;
    private final TokenIgnoreUrlRepository tokenIgnoreUrlRepository;

    public SecurityResourceService(ResourcesRepository resourcesRepository, AccessIpRepository accessIpRepository, TokenIgnoreUrlRepository tokenIgnoreUrlRepository) {
        this.resourcesRepository = resourcesRepository;
        this.accessIpRepository = accessIpRepository;
        this.tokenIgnoreUrlRepository = tokenIgnoreUrlRepository;
    }

    @Cacheable(value = "resourceList")
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        log.warn("cache test resourceList");
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAllResources();

        resourcesList.forEach(re ->
                {
                    List<ConfigAttribute> configAttributeList = new ArrayList<>();
                    re.getRoleSet().forEach(ro -> {
                        configAttributeList.add(new SecurityConfig(ro.getRole().name()));
                        result.put(new AntPathRequestMatcher(re.getResourceName()), configAttributeList);
                    });
                }
        );
        return result;
    }

    @Cacheable(value = "accessIpList")
    public List<AccessIp> getAccessIpList() {
        log.warn("cache test accessIpList");
        return accessIpRepository.findAll();
    }

    public boolean shouldIgnore(String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        String uri = URI.create(url).getPath();

        return tokenIgnoreUrlRepository.getTokenIgnoreUrls()
                .stream()
                .anyMatch(r -> matcher.match(r.getUrl(), uri) && Boolean.TRUE.equals(r.getIsIgnore()));
    }

}
