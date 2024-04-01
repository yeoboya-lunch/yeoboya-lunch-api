package com.yeoboya.lunch.api.v1.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.file.domain.MemberProfileFile;
import com.yeoboya.lunch.api.v1.member.domain.MemberInfo;
import com.yeoboya.lunch.api.v1.member.response.MemberResponse;
import com.yeoboya.lunch.api.v1.member.response.QMemberResponse;
import com.yeoboya.lunch.api.v1.member.response.QMemberRoleResponse;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import com.yeoboya.lunch.api.v1.member.response.MemberRoleResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.yeoboya.lunch.api.v1.file.domain.QMemberProfileFile.memberProfileFile;
import static com.yeoboya.lunch.api.v1.member.domain.QAccount.account;
import static com.yeoboya.lunch.api.v1.member.domain.QMember.member;
import static com.yeoboya.lunch.api.v1.member.domain.QMemberInfo.memberInfo;
import static com.yeoboya.lunch.config.security.domain.QMemberRole.memberRole;
import static com.yeoboya.lunch.config.security.domain.QRole.role1;
import static com.yeoboya.lunch.config.security.domain.QUserSecurityStatus.userSecurityStatus;


@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Slice<MemberResponse> findMembersInPages(Pageable pageable) {
        List<MemberResponse> content = query.select(
                        new QMemberResponse(
                                member.email, member.name,
                                account.bankName, account.accountNumber,
                                memberInfo.bio, memberInfo.nickName, memberInfo.phoneNumber
                        )
                )
                .from(member)
                .leftJoin(member.account, account)
                .leftJoin(member.memberInfo, memberInfo)
                .limit(pageable.getPageSize() + 1)  //페이지 사이즈
                .offset(pageable.getOffset())   //페이지번호
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }


    @Override
    public List<MemberRole> getMemberRoles(Long id) {
        return query.selectFrom(memberRole)
                .leftJoin(memberRole.member, member)
                .leftJoin(memberRole.role, role1)
                .where(memberRole.member.id.eq(id))
                .fetch();
    }

    @Override
    public MemberInfo getMemberInfo(String email) {
        return query.selectFrom(memberInfo)
                .leftJoin(memberInfo.member, member)
                .where(memberInfo.member.email.eq(email))
                .fetchOne();
    }

    @Override
    public MemberResponse memberProfile(String memberEmail) {
        List<MemberProfileFile> memberProfileFiles = query
                .select(memberProfileFile)
                .from(member)
                .leftJoin(member.memberProfileFiles, memberProfileFile)
                .where(member.email.eq(memberEmail).and(memberProfileFile.isNotNull()))
                .fetch();

        //Processing the data to generate profileImg for each MemberProfileFile
        //Check if profile files is not null else return empty list
        List<String> profileImages = !memberProfileFiles.isEmpty() ? memberProfileFiles.stream()
                .map(file -> file.getFilePath() + "/" + file.getFileName())
                .collect(Collectors.toList()) : Collections.emptyList();


        // Based on your current implementation, assuming we can still fetch one record for the rest
        MemberResponse memberResponse = query.select(
                        new QMemberResponse(
                                member.email, member.name,
                                account.bankName, account.accountNumber,
                                memberInfo.bio, memberInfo.nickName, memberInfo.phoneNumber
                        )
                )
                .from(member)
                .leftJoin(member.account, account)
                .leftJoin(member.memberInfo, memberInfo)
                .where(member.email.eq(memberEmail))
                .fetchOne();

        // Attach the profileImages to memberResponse
        if (memberResponse != null) {
            memberResponse.setProfileImages(profileImages);
        }
        return memberResponse;
    }

//    @Override
//    public MemberResponse memberProfile(String memberEmail) {
//        return query.select(
//                        new QMemberResponse(
//                                member.email, member.name,
//                                account.bankName, account.accountNumber,
//                                memberInfo.bio, memberInfo.nickName, memberInfo.phoneNumber, memberProfileFile.filePath, memberProfileFile.fileName
//                        )
//                )
//                .from(member)
//                .leftJoin(member.account, account)
//                .leftJoin(member.memberInfo, memberInfo)
//                .leftJoin(member.memberProfileFiles, memberProfileFile)
//                .where(memberInfo.member.email.eq(memberEmail))
//                .fetchOne();
//    }

    @Override
    public Page<MemberRoleResponse> findWithRolesInPages(Pageable pageable) {
        List<MemberRoleResponse> content = query.select(
                        new QMemberRoleResponse(
                                member.email, member.name, role1.roleDesc,
                                userSecurityStatus.isEnabled, userSecurityStatus.isAccountNonLocked
                        )
                )
                .from(member)
                .leftJoin(member.memberRoles, memberRole)
                .leftJoin(memberRole.role, role1)
                .leftJoin(member.userSecurityStatus, userSecurityStatus)
                .limit(pageable.getPageSize())  //페이지 사이즈
                .offset(pageable.getOffset())   //페이지번호
                .distinct()
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(member.countDistinct())
                .from(member)
                .leftJoin(member.memberRoles, memberRole)
                .leftJoin(memberRole.role, role1)
                .leftJoin(member.userSecurityStatus, userSecurityStatus);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


    private BooleanExpression likeMemberEmail(String email) {
        if (StringUtils.hasText(email)) {
            return member.email.like("%" + email + "%");
        }
        return null;
    }
}
