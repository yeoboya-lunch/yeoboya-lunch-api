package com.yeoboya.lunch.api.v1.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

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
    private String profileImg;
    private List<String> profileImages;

    @QueryProjection
    public MemberResponse(String email, String name, String bankName, String accountNumber, String bio, String nickName, String phoneNumber){
        this.email = email;
        this.name = name;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.bio = bio;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
    }

    @QueryProjection
    public MemberResponse(String email, String name, String bankName, String accountNumber, String bio, String nickName, String phoneNumber, List<String> profileImages) {
        this.email = email;
        this.name = name;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.bio = bio;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.profileImages = profileImages;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberResponse)) return false;
        MemberResponse that = (MemberResponse) o;
        return isAccount == that.isAccount &&
                Objects.equals(email, that.email) &&
                Objects.equals(name, that.name) &&
                Objects.equals(bankName, that.bankName) &&
                Objects.equals(accountNumber, that.accountNumber) &&
                Objects.equals(bio, that.bio) &&
                Objects.equals(nickName, that.nickName) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, bankName, accountNumber, bio, nickName, phoneNumber, isAccount);
    }
}
