package com.yeoboya.lunch.config.security.voter;

import com.yeoboya.lunch.config.security.service.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class IgnoreUrlVoter implements AccessDecisionVoter<Object> {

    private final SecurityResourceService securityResourceService;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> configList) {
        if (!(object instanceof FilterInvocation)) {
            return ACCESS_ABSTAIN;
        }

        String uri = ((FilterInvocation) object).getRequestUrl();

        // securityResourceService의 shouldIgnore 메소드를 사용하여 URI가 무시되어야 하는지 결정합니다.
        // 만약 무시되어야 한다면 ACCESS_GRANTED를 반환하고 그렇지 않다면 ACCESS_ABSTAIN을 반환합니다.
        return securityResourceService.shouldIgnore(uri) ? ACCESS_GRANTED : ACCESS_ABSTAIN;
    }
}
