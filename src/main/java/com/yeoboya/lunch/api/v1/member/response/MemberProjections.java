package com.yeoboya.lunch.api.v1.member.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MemberProjections {

    public interface MemberSummary {
        String getLoginId();
        String getEmail();
        String getName();

        default String getLoginIdEmailName(){
            return getLoginId() + "," + getEmail() + "," + getName();
        }
    }

    public interface MemberAccount {
        String getLoginId();
        String getEmail();
        String getName();
        default String getBankName(){
            return getAccountBankName();
        }
        default String getAccountNumber(){
            return getAccountAccountNumber();
        }
        @JsonIgnore
        String getAccountBankName();
        @JsonIgnore
        String getAccountAccountNumber();
    }

    public interface MemberApiKey{
        @JsonIgnore
        String getApiKey_ApiKey();

        default String getApiKey(){
            return getApiKey_ApiKey();
        }
    }
}
