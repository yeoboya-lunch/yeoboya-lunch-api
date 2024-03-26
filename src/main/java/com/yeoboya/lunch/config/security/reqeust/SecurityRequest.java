package com.yeoboya.lunch.config.security.reqeust;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SecurityRequest {
    private String email;
    private boolean isEnabled;
    private boolean isAccountNonLocked;

}
