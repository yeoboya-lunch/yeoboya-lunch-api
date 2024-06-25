package com.yeoboya.lunch.api.v1.member.repository;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {


    Optional<Member> findByEmail(String email);

    Optional<Member> findByLoginId(String loginId);

    <T> T findByEmail(String email, Class<T> type);

    <T> T findByLoginId(String loginId, Class<T> type);

    boolean existsMemberByLoginId(String loginId);

    boolean existsByEmail(String email);

    boolean existsByEmailAndProvider(String email, String provider);

    boolean existsMemberByEmailAndMemberInfoPhoneNumber(String email, String phoneNumber);

}
