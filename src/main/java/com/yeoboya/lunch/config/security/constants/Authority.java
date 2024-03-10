package com.yeoboya.lunch.config.security.constants;

import org.springframework.security.core.GrantedAuthority;

// Authority는 Spring Security가 제공하는 GrantedAuthority 인터페이스를 구현하는 열거형 클래스입니다.
// 각 열거형 상수는 사용자에게 할당할 수 있는 역할(권한)을 나타냅니다.
public enum Authority implements GrantedAuthority {

    ROLE_ADMIN,
    ROLE_MANAGER,
    ROLE_USER,
    ROLE_GUEST,
    ROLE_BLOCK;


    // GrantedAuthority 인터페이스에 요구되는 메서드입니다.
    // 권한(역할)을 문자열로 반환합니다.
    public String getAuthority() {
        return name();
    }
}
