package com.yeoboya.lunch.api.v1.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberRoleResponse {

    private String loginId;
    private String email;
    private String provider;
    private String name;
    private boolean isEnabled;
    private boolean isAccountNonLocked;

    @QueryProjection
    public MemberRoleResponse(String loginId, String email, String provider, String name, boolean isEnabled, boolean isAccountNonLocked){
        this.loginId = loginId;
        this.email = email;
        this.provider = provider;
        this.name = name;
        this.isEnabled = isEnabled;
        this.isAccountNonLocked = isAccountNonLocked;
    }

}
