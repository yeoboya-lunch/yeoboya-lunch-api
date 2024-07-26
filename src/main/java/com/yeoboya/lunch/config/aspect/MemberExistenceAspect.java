package com.yeoboya.lunch.config.aspect;

import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MemberExistenceAspect {

    private final MemberRepository memberRepository;

    public MemberExistenceAspect(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Around("@annotation(com.yeoboya.lunch.config.annotation.EnsureMemberExists)")
    public Object ensureMemberExists(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof String) {
            String loginId = (String) args[0];
            memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new EntityNotFoundException("Member not found - " + loginId));
        }
        return joinPoint.proceed();
    }
}
