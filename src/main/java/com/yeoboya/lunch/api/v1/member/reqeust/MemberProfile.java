package com.yeoboya.lunch.api.v1.member.reqeust;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class MemberProfile {

    private String loginId;
    private String subDirectory;
}
