package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.reqeust.UserRequest.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUp.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUp signUp = (SignUp) target;

//        if (memberRepository.findByEmail(SignUp.getEmail()).isEmpty()) {
//            errors.rejectValue("email", "invalid.email", new Object[]{signUp.getEmail()}, "이미 사용중인 이메일입니다.");
//        }

        if (memberRepository.existsByEmail(signUp.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUp.getEmail()}, "이미 사용중인 이메일입니다.");
        }
    }
}
