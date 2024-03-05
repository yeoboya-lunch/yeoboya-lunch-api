package com.yeoboya.lunch.config.aspect;

import com.yeoboya.lunch.config.security.metaDataSource.UrlSecurityMetadataSource;
import com.yeoboya.lunch.config.security.service.SecurityConfigChangePublisher;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SecurityConfigChangeAspect {

    private final SecurityConfigChangePublisher publisher;

    @AfterReturning(pointcut = "execution(* com.yeoboya.lunch.config.security.service.ResourcesService.*(..)) " +
            "|| execution(* com.yeoboya.lunch.config.security.service.RoleService.*(..))")
    public void afterSecurityConfigChanged(JoinPoint joinPoint) {
        publisher.publishSecurityConfigChanged();
    }
}
