package com.yeoboya.guinGujik.config.security.repository;

import com.yeoboya.guinGujik.config.security.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}