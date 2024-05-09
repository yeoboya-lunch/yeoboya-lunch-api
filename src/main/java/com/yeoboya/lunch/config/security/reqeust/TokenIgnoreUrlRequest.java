package com.yeoboya.lunch.config.security.reqeust;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenIgnoreUrlRequest {
    private boolean isIgnore;
    private String url;
}
