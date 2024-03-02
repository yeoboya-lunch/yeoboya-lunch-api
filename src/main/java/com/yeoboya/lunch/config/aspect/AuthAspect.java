package com.yeoboya.lunch.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class AuthAspect {


    @Around("@annotation(com.yeoboya.lunch.config.annotation.Auth)")
    public Object compareJwtTokenSubjectAndRequestBodyEmail(ProceedingJoinPoint pjp) throws Throwable {
        String proceedName = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
        log.info("[" + proceedName + "] 시작");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication.getName());

        Object result = pjp.proceed();
        stopWatch.stop();
        log.info("[" + proceedName + "] 실행시간 : " + stopWatch.getTotalTimeMillis() + " (ms)");
        return result;
    }
}
