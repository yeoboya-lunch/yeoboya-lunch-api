package com.yeoboya.lunch.config.security.reqeust;


import com.yeoboya.lunch.config.security.constants.Authority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequest {
    private String email;
    private Authority role;
    private boolean isEnabled;
    private boolean isAccountNonLocked;
}
