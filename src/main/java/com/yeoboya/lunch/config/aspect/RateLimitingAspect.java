package com.yeoboya.lunch.config.aspect;

import com.yeoboya.lunch.api.v1.common.response.ErrorCode;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.api.v1.member.response.MemberProjections;
import com.yeoboya.lunch.config.pricingPlan.service.PricingPlanService;
import com.yeoboya.lunch.config.annotation.RateLimited;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class RateLimitingAspect {

    private final PricingPlanService pricingPlanService;
    private final Response response;
    private final MemberRepository memberRepository;

    public RateLimitingAspect(PricingPlanService pricingPlanService, Response response, MemberRepository memberRepository) {
        this.pricingPlanService = pricingPlanService;
        this.response = response;
        this.memberRepository = memberRepository;
    }

    @Around("@annotation(rateLimited)")
    public Object rateLimit(ProceedingJoinPoint pjp, RateLimited rateLimited) throws Throwable {

        //fixme request 회원정보 가져오기.
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(sra).getRequest();
        MemberProjections.MemberApiKey memberApiKey = memberRepository.findByEmail("admin@lunch.com", MemberProjections.MemberApiKey.class);
        String apiKey= memberApiKey.getApiKey();

        long numTokens=rateLimited.limit();
        Bucket bucket = pricingPlanService.resolveBucket(apiKey);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(numTokens);
        long saveToken = probe.getRemainingTokens();

        if (probe.isConsumed()) {
            return pjp.proceed();
        }

        long nanoSeconds = probe.getNanosToWaitForRefill();
        long hours = TimeUnit.NANOSECONDS.toHours(nanoSeconds);
        nanoSeconds -= TimeUnit.HOURS.toNanos(hours);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(nanoSeconds);
        nanoSeconds -= TimeUnit.MINUTES.toNanos(minutes);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(nanoSeconds);

        log.error("TOO MANY REQUEST Wait Time {} hours {} minutes {} seconds / Available {}",
                hours, minutes, seconds, saveToken);

        return response.fail(ErrorCode.TOO_MANY_REQUESTS);
    }
}
