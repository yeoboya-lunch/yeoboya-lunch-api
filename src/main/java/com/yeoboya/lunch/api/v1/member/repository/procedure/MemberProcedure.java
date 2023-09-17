package com.yeoboya.lunch.api.v1.member.repository.procedure;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.response.procedure.MemberResponseInterface;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberProcedure extends JpaRepository<Member, Long> {

    @Procedure(procedureName= "db_khj.p_member_list")
    List<MemberResponseInterface> pMemberList(int pageNo, int pagePerCnt);

    @Procedure(procedureName= "db_khj.p_member_list_by_email")
    List<MemberResponseInterface> pMemberListByEmail(@Param("email") String email);
}
