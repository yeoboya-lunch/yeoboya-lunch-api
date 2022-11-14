package com.yeoboya.lunch.api.v1.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.yeoboya.lunch.api.v1.member.domain.QAccount.account;
import static com.yeoboya.lunch.api.v1.member.domain.QMember.member;


@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Member> getList(Pageable pageable) {
        return jpaQueryFactory.selectFrom(member)
                .leftJoin(member.account, account).fetchJoin()
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }
}
