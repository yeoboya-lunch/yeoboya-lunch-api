package com.yeoboya.guinGujik.config.security.service;

import com.yeoboya.guinGujik.config.security.domain.Member;
import com.yeoboya.guinGujik.config.security.dto.Users;
import com.yeoboya.guinGujik.config.security.repository.UsersJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersJpaRepository usersJpaRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersJpaRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found - " + username));
    }

    private UserDetails createUserDetails(Member member) {
        return Users.builder().
                email(member.getEmail()).
                password(member.getPassword()).
                roles(Collections.singletonList(member.getRole().toString())).
                build();
    }

}