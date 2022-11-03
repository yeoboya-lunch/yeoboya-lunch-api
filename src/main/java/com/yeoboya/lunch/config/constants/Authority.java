package com.yeoboya.lunch.config.constants;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_CLIENT;

    public String getAuthority() {
        return name();
    }

}