package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.domain.UserSecurityStatus;
import com.yeoboya.lunch.config.security.dto.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found - " + username));
    }

    private UserDetails createUserDetails(Member member) {
        List<String> roles = new ArrayList<>();

        memberRepository.getMemberRoles(member.getId())
                .forEach(memberRole->roles.add(memberRole.getRole().getRole().getAuthority()));

        UserSecurityStatus userSecurityStatus = memberRepository.findByEmail(member.getEmail())
                .map(Member::getUserSecurityStatus)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return Users.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .enabled(userSecurityStatus.isEnabled())
                .lock(userSecurityStatus.isAccountNonLocked())
                .roles(roles)
                .build();
    }

}
