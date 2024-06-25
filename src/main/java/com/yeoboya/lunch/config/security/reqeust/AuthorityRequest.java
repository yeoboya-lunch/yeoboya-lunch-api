package com.yeoboya.lunch.config.security.reqeust;

import com.yeoboya.lunch.config.security.constants.Authority;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AuthorityRequest {

    @NotEmpty(message = "로그인 아이디는 필수 입력값입니다.")
    private String loginId;
    private Authority role;
}

