package com.yeoboya.lunch.api.v2.dalla.request;

import lombok.Data;

@Data
public class SocketVo {
    private String memNo;
    private int login;
    private String command;
    private String message;
    private String ctrlRole;
    private String recvType;
    private int auth;
    private String authName;

    public String toQueryString() {
        return "command=" + this.command +
                "&memNo=" + this.memNo +
                "&message=" + (this.message == null ? "" : this.message) +
                "&auth=" + this.auth +
                "&authName=" + this.authName +
                "&ctrlRole=" + (this.ctrlRole == null ? "" : this.ctrlRole) +
                "&login=" + this.login +
                "&recvType=" + this.recvType;
    }
}
