package com.yeoboya.lunch.config.aspect;

import com.yeoboya.lunch.config.security.metaDataSource.UrlSecurityMetadataSource;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SecurityConfigChangeAspect {

    private final UrlSecurityMetadataSource urlSecurityMetadataSource;

    @AfterReturning(pointcut = "execution(* com.yeoboya.lunch.config.security.service.ResourcesService.*(..)) " +
            "|| execution(* com.yeoboya.lunch.config.security.service.RoleService.*(..))")
    public void afterSecurityConfigChanged(JoinPoint joinPoint) {
        urlSecurityMetadataSource.reload();
    }
}
