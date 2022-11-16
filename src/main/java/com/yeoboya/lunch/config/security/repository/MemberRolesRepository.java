package com.yeoboya.lunch.config.security.repository;

import com.yeoboya.lunch.config.security.domain.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRolesRepository extends JpaRepository<MemberRole, Long> {
}
