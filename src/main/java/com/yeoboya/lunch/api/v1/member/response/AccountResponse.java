package com.yeoboya.lunch.api.v1.member.response;

import com.yeoboya.lunch.api.v1.member.domain.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountResponse {

    private String name;
    private String bankName;
    private String accountNumber;

    @Builder
    public AccountResponse(String bankName, String accountNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

    @Builder
    public AccountResponse(Account account){
        this.name = account.getMember().getName();
        this.bankName = account.getBankName();
        this.accountNumber = account.getAccountNumber();
    }
}