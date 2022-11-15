package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.api.v1.exception.MemberNotFound;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import com.yeoboya.lunch.config.security.dto.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found - " + username));
    }

    //fixme get roles
    private UserDetails createUserDetails(Member member) {

        //fixme 여기서 Roles 조회?
        // member id로

        List<String> roles = new ArrayList<>();
        Member member1 = memberRepository.authMember(member.getEmail()).orElseThrow(MemberNotFound::new);


        List<MemberRole> memberRoles = member1.getMemberRoles();
        for (MemberRole role : memberRoles) {

        }


        return Users.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .roles(roles)
                .build();
    }


}