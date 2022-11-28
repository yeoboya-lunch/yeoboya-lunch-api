package com.yeoboya.lunch.api.v1.member.response;

import com.yeoboya.lunch.api.v1.member.domain.MemberInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberInfoResponse {

    private final String bio;
    private final String nickName;
    private final String phoneNumber;

    public static MemberInfoResponse from(MemberInfo memberInfo) {
        return new MemberInfoResponse(memberInfo.getBio(), memberInfo.getNickName(), memberInfo.getPhoneNumber());
    }
}
