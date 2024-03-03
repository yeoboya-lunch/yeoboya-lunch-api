package com.yeoboya.lunch.config.security.repository;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRolesRepository extends JpaRepository<MemberRole, Long> {

    List<MemberRole> findByMemberEmail(String email);

}
