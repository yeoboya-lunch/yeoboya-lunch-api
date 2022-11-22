package com.yeoboya.lunch.api.v1.member.response;

import com.yeoboya.lunch.api.v1.member.domain.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountResponse {

    private final String bankName;
    private final String accountNumber;

    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getBankName(), account.getAccountNumber());
    }
}