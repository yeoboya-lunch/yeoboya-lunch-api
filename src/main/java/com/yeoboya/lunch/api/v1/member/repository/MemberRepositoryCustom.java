package com.yeoboya.lunch.api.v1.member.repository;

import com.yeoboya.lunch.api.v1.file.domain.MemberProfileFile;
import com.yeoboya.lunch.api.v1.member.domain.MemberInfo;
import com.yeoboya.lunch.api.v1.member.response.MemberResponse;
import com.yeoboya.lunch.api.v1.member.response.MemberRoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface MemberRepositoryCustom {

    Slice<MemberResponse> findMembersInPages(Pageable pageable);

    MemberInfo getMemberInfo(String loginId);

    MemberResponse memberProfile(String loginId);

    List<MemberProfileFile> profileImg(String loginId);

    Page<MemberRoleResponse> findWithRolesInPages(Pageable pageable);
}
