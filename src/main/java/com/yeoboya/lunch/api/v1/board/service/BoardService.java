package com.yeoboya.lunch.api.v1.board.service;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.repository.BoardRepository;
import com.yeoboya.lunch.api.v1.board.repository.HashTagRepository;
import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.common.response.ErrorCode;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.*;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final HashTagRepository hashTagRepository;
    private final MemberRepository memberRepository;
    private final Response response;

    public BoardService(BoardRepository boardRepository, HashTagRepository hashTagRepository, MemberRepository memberRepository, Response response) {
        this.boardRepository = boardRepository;
        this.hashTagRepository = hashTagRepository;
        this.memberRepository = memberRepository;
        this.response = response;
    }

    public ResponseEntity<Body> saveBoard(BoardCreate boardCreate) {
        if (memberRepository.findByEmail(boardCreate.getEmail()).isEmpty()) {
            return response.fail(ErrorCode.USER_NOT_FOUND);
        }

        String name = Optional.of(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException(""));
        if (!boardCreate.getEmail().equals(name)) {
            return response.fail(ErrorCode.INVALID_AUTH_TOKEN);
        }

        //기존 hashtag 존재하는지 확인
//        boardCreate.getHashTag().forEach(r->hashTagRepository.findHashTagByValue(r));

        Board board = Board.







        return null;
    }
}
