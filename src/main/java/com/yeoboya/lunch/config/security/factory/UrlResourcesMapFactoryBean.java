package com.yeoboya.lunch.config.security.factory;

import com.yeoboya.lunch.config.security.service.SecurityResourceService;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    @Setter
    private SecurityResourceService securityResourceService;

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourcesMap;

    public void init() {
            resourcesMap = securityResourceService.getResourceList();
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() {
        if (resourcesMap == null) {
            init();
        }
        return resourcesMap;
    }

	public Class<LinkedHashMap> getObjectType() {
        return LinkedHashMap.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
