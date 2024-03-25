package com.yeoboya.lunch.api.v1.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberRoleResponse {

    private String email;
    private String name;
    private String roleDesc;
    private boolean isEnabled;
    private boolean isAccountNonLocked;

    @QueryProjection
    public MemberRoleResponse(String email, String name, String roleDesc, boolean isEnabled, boolean isAccountNonLocked){
        this.email = email;
        this.name = name;
        this.roleDesc = roleDesc;
        this.isEnabled = isEnabled;
        this.isAccountNonLocked = isAccountNonLocked;
    }

}
