package com.yeoboya.lunch.api.v1.member.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountResponse {

    private String bankName;
    private String accountNumber;

    @Builder
    public AccountResponse(String bankName, String accountNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }
}
