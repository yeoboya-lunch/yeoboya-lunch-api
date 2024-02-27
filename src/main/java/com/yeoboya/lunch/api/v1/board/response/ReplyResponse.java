package com.yeoboya.lunch.api.v1.board.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeoboya.lunch.api.v1.board.domain.Reply;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ReplyResponse {

    private Long replyId;
    private String writer;
    private String parentContent;
    private Date date;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ReplyResponse> childReplies = new ArrayList<>();

    public static ReplyResponse from(Member member, Reply reply, List<Reply> childReplies) {
        ReplyResponse replyResponse = new ReplyResponse();
        replyResponse.setReplyId(reply.getId());
        replyResponse.setWriter(member.getName());
        replyResponse.setParentContent(reply.getContent());
        replyResponse.setDate(reply.getCreateDate());

        for (Reply childReply : childReplies) {
            if (childReply.getParentReply() != null && childReply.getParentReply().getId().equals(reply.getId())) {
                replyResponse.getChildReplies().add(from(member, childReply, childReplies));
            }
        }
        return replyResponse;
    }

}
