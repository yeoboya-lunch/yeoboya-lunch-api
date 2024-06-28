package com.yeoboya.lunch.api.v1.support.request;

import lombok.Data;

@Data
public class InquiryRequest {
    private String email;
    private String loginId;
    private String subject;
    private String content;
}
