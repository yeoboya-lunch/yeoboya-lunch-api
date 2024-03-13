package com.yeoboya.lunch.config.security.service;


import com.yeoboya.lunch.config.security.domain.AccessIp;
import com.yeoboya.lunch.config.security.domain.Resources;
import com.yeoboya.lunch.config.security.repository.AccessIpRepository;
import com.yeoboya.lunch.config.security.repository.ResourcesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
public class SecurityResourceService {

    private final ResourcesRepository resourcesRepository;
    private final AccessIpRepository accessIpRepository;

    public SecurityResourceService(ResourcesRepository resourcesRepository, AccessIpRepository accessIpRepository) {
        this.resourcesRepository = resourcesRepository;
        this.accessIpRepository = accessIpRepository;
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

}
