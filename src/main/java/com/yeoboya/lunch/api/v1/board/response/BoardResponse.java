package com.yeoboya.lunch.api.v1.board.response;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class BoardResponse {

    private final String title;
    private final String content;
    private final int pin;
    private final boolean secret;
    private final String email;
    private final List<HashTagResponse> hashTags;
    private final List<FileUploadResponse> files;

    public static BoardResponse from(Board board){
        return new BoardResponse(
                board.getTitle(), board.getContent(), board.getPin(), board.isSecret(), board.getMember().getEmail(),
                board.getBoardHashTags().stream().map(r-> HashTagResponse.from(r.getHashTag())).collect(Collectors.toList()),
                board.getFiles().stream().map(FileUploadResponse::from).collect(Collectors.toList())
        );
    }
}
