package com.yeoboya.lunch.api.v1.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeoboya.lunch.api.v1.member.domain.Account;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.Getter;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponse {

    private final String email;
    private final String name;
    private AccountResponse account;

    public MemberResponse(Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        if(member.getAccount()!=null) {
            this.account = toAccountResponse(member.getAccount());
        }
    }

    public AccountResponse toAccountResponse(Account account){
        return AccountResponse.builder()
                .bankName(account.getBankName())
                .accountNumber(account.getAccountNumber())
                .build();
    }
}
