package com.yeoboya.lunch.api.v1.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.member.domain.MemberInfo;
import com.yeoboya.lunch.api.v1.member.response.MemberResponse;
import com.yeoboya.lunch.api.v1.member.response.QMemberResponse;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.yeoboya.lunch.api.v1.member.domain.QAccount.account;
import static com.yeoboya.lunch.api.v1.member.domain.QMember.member;
import static com.yeoboya.lunch.api.v1.member.domain.QMemberInfo.memberInfo;
import static com.yeoboya.lunch.config.security.domain.QMemberRole.memberRole;
import static com.yeoboya.lunch.config.security.domain.QRoles.roles;


@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberResponse> getMembers(Pageable pageable) {
        return jpaQueryFactory.select(
                        new QMemberResponse(
                                member.email, member.name,
                                account.bankName, account.accountNumber,
                                memberInfo.bio, memberInfo.nickName, memberInfo.phoneNumber
                        )
                )
                .from(member)
                .leftJoin(member.account, account)
                .leftJoin(member.memberInfo, memberInfo)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }


    @Override
    public List<MemberRole> getMemberRoles(Long id) {
        return jpaQueryFactory.selectFrom(memberRole)
                .leftJoin(memberRole.member, member)
                .leftJoin(memberRole.roles, roles)
                .where(memberRole.member.id.eq(id))
                .fetch();
    }

    @Override
    public MemberInfo getMemberInfo(String email) {
        return jpaQueryFactory.selectFrom(memberInfo)
                .leftJoin(memberInfo.member, member)
                .where(memberInfo.member.email.eq(email))
                .fetchOne();
    }

    @Override
    public MemberResponse memberProfile(String memberEmail) {
        return jpaQueryFactory.select(
                        new QMemberResponse(
                                member.email, member.name,
                                account.bankName, account.accountNumber,
                                memberInfo.bio, memberInfo.nickName, memberInfo.phoneNumber
                        )
                )
                .from(member)
                .leftJoin(member.account, account)
                .leftJoin(member.memberInfo, memberInfo)
                .where(memberInfo.member.email.eq(memberEmail))
                .fetchOne();
    }


    private BooleanExpression likeMemberEmail(String email) {
        if (StringUtils.hasText(email)) {
            return member.email.like("%" + email + "%");
        }
        return null;
    }
}
