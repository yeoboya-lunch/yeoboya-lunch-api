package com.yeoboya.lunch.config.security.response;

import com.yeoboya.lunch.config.security.constants.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorityDTO {
    private String authority;

    public AuthorityDTO(Authority authority) {
        this.authority = authority.name();
    }
}
