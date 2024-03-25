package com.yeoboya.lunch.config.security.dto;

import com.yeoboya.lunch.api.v1.common.exception.AuthorityException;
import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import lombok.*;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Users implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String email;
    private String password;
    private Boolean lock;
    private Boolean enabled;

    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword(){
        return this.password;
    }


    //계정이 만료되지 않았는지 여부를 반환합니다.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있지 않은지 여부를 반환합니다
    @Override
    public boolean isAccountNonLocked() {
        if(!this.lock) {
            throw new AuthorityException("사용자 계정이 잠겨 있습니다.");
        }
        return true;
    }

    //인증 정보(주로 비밀번호)가 만료되지 않았는지 여부를 반환합니다
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용 가능) 상태인지 여부를 반환합니다.
    @Override
    public boolean isEnabled() {
        if(!this.enabled) {
            throw new AuthorityException("계정이 비활성화 상태입니다. 관리자에게 문의하세요.");
        }
        return true;
    }
}
