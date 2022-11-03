package com.yeoboya.lunch.config.aspect;

import com.yeoboya.lunch.config.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class RetryAspect {

    @Aspect
    @Component
    @Order(1)
    public static class ReTry {
        @Around("@annotation(retry)")
        public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
            log.debug("[retry] {} retry={}", joinPoint.getSignature(), retry);
            int maxRetry = retry.value();
            Exception exceptionHolder = null;
            for (int retryCount = 0; retryCount <= maxRetry; retryCount++) {
                try {
                    log.error("[retry] 재시도 {} / 최대 재시도 {}", retryCount, maxRetry);
                    return joinPoint.proceed();
                } catch (Exception e) {
                    exceptionHolder = e;
                }
            }
            throw exceptionHolder;
        }
    }

    @Aspect
    @Component
    @Order(2)
    public static class Trace{
        @Before("@annotation(com.yeoboya.lunch.config.annotation.Trace)")
        public void doTrace(JoinPoint joinPoint) {
            Object[] args = joinPoint.getArgs();
            log.info("[trace] {} args={}", joinPoint.getSignature(), args);
        }
    }
}