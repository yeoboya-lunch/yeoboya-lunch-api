package com.yeoboya.lunch.config.security.reqeust;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@ToString
public class SecurityRequest {

    @NotEmpty(message = "로그인 아이디는 필수 입력값입니다.")
    private String loginId;
    private boolean isEnabled;
    private boolean isAccountNonLocked;

}
