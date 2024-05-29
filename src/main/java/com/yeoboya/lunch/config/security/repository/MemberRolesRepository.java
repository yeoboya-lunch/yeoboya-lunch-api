package com.yeoboya.lunch.config.security.repository;

import com.yeoboya.lunch.config.security.domain.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRolesRepository extends JpaRepository<MemberRole, Long> {

    Optional<MemberRole> findByMemberEmail(String email);

}
