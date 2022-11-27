package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.reqeust.UserRequest;
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
        return clazz.isAssignableFrom(UserRequest.Credentials.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequest.Credentials credentials = (UserRequest.Credentials) target;

        Member member = memberRepository.findByEmail(credentials.getEmail()).
                orElseThrow(()->new EntityNotFoundException("Member not found - " + credentials.getEmail()));

        if (!passwordEncoder.matches(credentials.getOldPassword(), member.getPassword())){
            errors.rejectValue("oldPassword", "wrong.password", "Old password isn't valid");
        }
    }


}