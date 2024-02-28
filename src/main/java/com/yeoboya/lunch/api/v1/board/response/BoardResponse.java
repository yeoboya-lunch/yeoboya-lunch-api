package com.yeoboya.lunch.api.v1.board.response;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.domain.Reply;
import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class BoardResponse {

    private final Long boardId;
    private final String title;
    private final String content;
    private final boolean secret;
    private final String email;
    private final String name;
    private final String createDate;
    private final List<HashTagResponse> hashTags;
    private final List<FileUploadResponse> files;
    private final List<ReplyResponse> replies;
    private long replyCount;

    public static BoardResponse from(Board board) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 a HH:mm");
        return new BoardResponse(
                board.getId(), board.getTitle(), board.getContent(), board.isSecret(), board.getMember().getEmail(),
                board.getMember().getName(), simpleDateFormat.format(board.getCreateDate()),
                board.getBoardHashTags().stream().map(r -> HashTagResponse.from(r.getHashTag())).collect(Collectors.toList()),
                board.getFiles().stream().map(FileUploadResponse::from).collect(Collectors.toList()),
                board.getReplies().stream().map(r -> ReplyResponse.of(board.getMember(), r, r.getBoard().getReplies())).collect(Collectors.toList())
        );
    }


    public static BoardResponse of(Board board, long replyCount) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 a HH:mm");
        BoardResponse response = new BoardResponse(
                board.getId(), board.getTitle(), board.getContent(), board.isSecret(), board.getMember().getEmail(),
                board.getMember().getName(), simpleDateFormat.format(board.getCreateDate()),
                board.getBoardHashTags().stream().map(r -> HashTagResponse.from(r.getHashTag())).collect(Collectors.toList()),
                board.getFiles().stream().map(FileUploadResponse::from).collect(Collectors.toList()),
                board.getReplies().stream().map(r -> ReplyResponse.of(board.getMember(), r, r.getBoard().getReplies())).collect(Collectors.toList())
        );
        response.setReplyCount(replyCount);
        return response;
    }

    public static BoardResponse of(Board board, long replyCount, Page<Reply> replies) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 a HH:mm");
        BoardResponse response = new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.isSecret(),
                board.getMember().getEmail(),
                board.getMember().getName(),
                simpleDateFormat.format(board.getCreateDate()),
                board.getBoardHashTags().stream().map(r -> HashTagResponse.from(r.getHashTag())).collect(Collectors.toList()),
                board.getFiles().stream().map(FileUploadResponse::from).collect(Collectors.toList()),
                replies.getContent().stream().map(r -> ReplyResponse.of(board.getMember(), r, r.getBoard().getReplies())).collect(Collectors.toList())
        );
        response.setReplyCount(replyCount);
        return response;
    }
}
