package com.yeoboya.lunch.api.v1.member.repository;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {


    Optional<Member> findByEmail(String email);

    <T> T findByEmail(String email, Class<T> type);

    boolean existsByEmail(String email);

    boolean existsMemberByEmailAndMemberInfoPhoneNumber(String email, String phoneNumber);

}
