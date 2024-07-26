package com.yeoboya.lunch.api.v1.file.repository;

import com.yeoboya.lunch.api.v1.file.domain.MemberProfileFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberProfileFileRepository extends JpaRepository<MemberProfileFile, Long> {

    @Query("SELECT mpf FROM MemberProfileFile mpf WHERE mpf.member.loginId = :loginId AND mpf.id = :id")
    MemberProfileFile findByMemberLoginIdAndId(@Param("loginId") String memberLoginId, @Param("id") Long id);

//    @Query("SELECT mpf FROM MemberProfileFile mpf WHERE mpf.member.loginId = :loginId AND mpf.isDefault = true")
    List<MemberProfileFile> findByMember_LoginIdAndIsDefaultTrue(@Param("loginId") String loginId);
}
