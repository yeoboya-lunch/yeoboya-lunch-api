package com.yeoboya.lunch.api.v1.member.repository;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.config.security.domain.Roles;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> getMembers(Pageable pageable);

    List<Roles> getMemberRoles(Long id);

}
