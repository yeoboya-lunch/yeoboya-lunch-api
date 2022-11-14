package com.yeoboya.lunch.config.security.constants;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    ROLE_ADMIN, ROLE_MANGER, ROLE_USER;

    public String getAuthority() {
        return name();
    }

}