package com.yeoboya.guinGujik.config.security.constants;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_CLIENT;

    public String getAuthority() {
        return name();
    }

}