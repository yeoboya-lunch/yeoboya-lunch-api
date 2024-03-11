package com.yeoboya.lunch.config.security.voter;

import com.yeoboya.lunch.config.security.domain.AccessIp;
import com.yeoboya.lunch.config.security.reqeust.ClientRequestInfo;
import com.yeoboya.lunch.config.security.service.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class IpAddressVoter implements AccessDecisionVoter<Object> {

    private final SecurityResourceService securityResourceService;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return (attribute.getAttribute() != null);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> configList) {
        if (!(authentication.getDetails() instanceof ClientRequestInfo)) {
            return ACCESS_DENIED;
        }

        String address = ((ClientRequestInfo) authentication.getDetails()).getRemoteIp();
        boolean isIpAddressBlocked = securityResourceService.getAccessIpList().stream()
                .filter(ip -> address.equals(ip.getIpAddress()))
                .anyMatch(AccessIp::isBlock);

        if (isIpAddressBlocked) {
            throw new AccessDeniedException("Invalid IP address: " + address + " cannot access this resource.");
        }

        return ACCESS_ABSTAIN;
    }
}
