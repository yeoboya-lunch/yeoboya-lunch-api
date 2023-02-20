package com.yeoboya.lunch.api.v1.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberResponse {

    private final String email;
    private final String name;
    private final String bankName;
    private final String accountNumber;
    private final String bio;
    private final String nickName;
    private final String phoneNumber;

    @QueryProjection
    public MemberResponse(String email, String name, String bankName, String accountNumber, String bio, String nickName, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.bio = bio;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getEmail(), member.getName(), member.getAccount().getBankName(), member.getAccount().getAccountNumber(),
                member.getMemberInfo().getBio(), member.getMemberInfo().getNickName(), member.getMemberInfo().getPhoneNumber());
    }
}
