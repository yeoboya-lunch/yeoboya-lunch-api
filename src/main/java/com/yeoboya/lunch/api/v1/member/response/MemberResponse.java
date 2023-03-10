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

    private String email;
    private String name;
    private String bankName;
    private String accountNumber;
    private String bio;
    private String nickName;
    private String phoneNumber;
    private boolean isAccount;

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

    public MemberResponse(String email, String name, String nickName, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getEmail(), member.getName(), member.getMemberInfo().getNickName(), member.getMemberInfo().getPhoneNumber());
    }
}
