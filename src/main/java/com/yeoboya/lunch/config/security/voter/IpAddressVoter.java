package com.yeoboya.lunch.config.security.voter;

import com.yeoboya.lunch.config.security.service.SecurityResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;
import java.util.List;

@Slf4j
public class IpAddressVoter implements AccessDecisionVoter<Object> {

    private final SecurityResourceService securityResourceService;

//    private static final String REQUEST_URI = "/admin/";

    public IpAddressVoter(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

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
        log.error("{}", authentication.getDetails());
        if (!(authentication.getDetails() instanceof WebAuthenticationDetails)) {
            return ACCESS_DENIED;
        }

        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String address = details.getRemoteAddress();
        log.error("{}", address);
        List<String> accessIpList = securityResourceService.getAccessIpList();

        log.error("{}", accessIpList);
        for (String ipAddress : accessIpList) {
            if (address.equals(ipAddress)) {
                return ACCESS_ABSTAIN;
            }
        }

        throw new AccessDeniedException("Invalid IP address: " + address + " cannot access this resource.");
    }
}
