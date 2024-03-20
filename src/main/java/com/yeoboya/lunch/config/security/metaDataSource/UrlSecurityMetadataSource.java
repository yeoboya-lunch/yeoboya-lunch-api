package com.yeoboya.lunch.config.security.metaDataSource;

import com.yeoboya.lunch.config.security.service.SecurityResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UrlSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;
    private final SecurityResourceService securityResourceService;

    public UrlSecurityMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap, SecurityResourceService securityResourceService) {
        this.requestMap = requestMap;
        this.securityResourceService = securityResourceService;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        FilterInvocation fi = (FilterInvocation) object;
        HttpServletRequest httpServletRequest = fi.getHttpRequest();

        if (requestMap == null) {
            return null;
        }

        return requestMap.entrySet().stream()
                .filter(entry -> entry.getKey().matches(httpServletRequest))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return requestMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    public void reload() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadedMap = securityResourceService.getResourceList();
        requestMap.clear();
        requestMap.putAll(reloadedMap);
        log.warn("Secured Url Resources - Role Mappings reloaded at Runtime!");
    }
}
