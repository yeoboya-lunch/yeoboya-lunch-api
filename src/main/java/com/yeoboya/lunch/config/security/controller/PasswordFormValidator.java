package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.reqeust.UserRequest.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class PasswordFormValidator implements Validator {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Password.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Password password = (Password) target;

        Member member = memberRepository.findByEmail(password.getEmail()).
                orElseThrow(()->new EntityNotFoundException("Member not found - " + password.getEmail()));

        if (!passwordEncoder.matches(password.getOldPassword(), member.getPassword())){
            errors.rejectValue("oldPassword", "wrong.password", "Old password isn't valid");
        }
    }


}