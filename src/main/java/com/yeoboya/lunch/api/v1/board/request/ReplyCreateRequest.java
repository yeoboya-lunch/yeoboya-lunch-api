package com.yeoboya.lunch.api.v1.board.request;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.implementation.bind.annotation.Empty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ReplyCreateRequest {

    @NotEmpty(message = "로그인 아이디는 필수 입력값입니다.")
    private String loginId;
    private Long boardId;
    private String content;
    private Long parentReplyId;

    @Override
    public String toString() {
        return "ReplyCreateRequest{" +
                "loginId='" + loginId + '\'' +
                ", boardId=" + boardId +
                ", content='" + content + '\'' +
                ", parentReplyId=" + parentReplyId +
                '}';
    }
}
