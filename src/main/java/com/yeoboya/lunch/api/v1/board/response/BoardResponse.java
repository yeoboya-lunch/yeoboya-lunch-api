package com.yeoboya.lunch.api.v1.board.response;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.domain.BoardHashTag;
import com.yeoboya.lunch.api.v1.file.domain.File;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class BoardResponse {

    private final String title;
    private final String content;
    private final int pin;
    private final boolean secret;
    private Member member;
    private final List<BoardHashTag> boardHashTags;
    private List<File> files;

    public static BoardResponse from(Board board){
//        return new BoardResponse(board.getTitle(), board.getContent(), board.getPin(), board.isSecret(),
//            board.getBoardHashTags().stream().map((r)->HashTagResponse.from(board.get))
        );
    }
}
