package com.yeoboya.lunch.api.v1.member.repository;

import com.yeoboya.lunch.api.v1.member.domain.MemberInfo;
import com.yeoboya.lunch.api.v1.member.response.MemberResponse;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberResponse> getMembers(Pageable pageable);

    List<MemberRole> getMemberRoles(Long id);

    MemberInfo getMemberInfo(String email);

}
