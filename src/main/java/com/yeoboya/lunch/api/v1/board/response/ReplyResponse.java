package com.yeoboya.lunch.api.v1.board.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeoboya.lunch.api.v1.board.domain.Reply;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class ReplyResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long parentId;

    private Long replyId;

    private String writer;

    private String content;

    private Date date;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ReplyResponse> childReplies = new ArrayList<>();

    public static ReplyResponse of(Member member, Reply reply, List<Reply> allReplies) {
        ReplyResponse replyResponse = new ReplyResponse();
        replyResponse.setReplyId(reply.getId());
        replyResponse.setWriter(member.getName());
        replyResponse.setContent(reply.getContent());
        replyResponse.setDate(reply.getCreateDate());

        Map<Long, List<Reply>> groupedReplies = allReplies.stream()
                .filter(r -> r.getParentReply() != null)
                .collect(Collectors.groupingBy(r -> r.getParentReply().getId()));

        List<Reply> childReplies = groupedReplies.get(reply.getId());

        if (childReplies != null) {
            for (Reply childReply : childReplies) {
                ReplyResponse childReplyResponse = of(member, childReply, childReplies);
                childReplyResponse.setParentId(reply.getId());
                replyResponse.getChildReplies().add(childReplyResponse);
            }
        }
        return replyResponse;
    }

}
