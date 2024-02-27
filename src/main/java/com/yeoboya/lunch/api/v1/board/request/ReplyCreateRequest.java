package com.yeoboya.lunch.api.v1.board.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class ReplyCreateRequest {
    @Email
    private String email;
    private Long boardId;
    private String content;
    private Long parentReplyId;

    @Override
    public String toString() {
        return "ReplyCreateRequest{" +
                "email='" + email + '\'' +
                ", boardId=" + boardId +
                ", content='" + content + '\'' +
                ", parentReplyId=" + parentReplyId +
                '}';
    }
}
