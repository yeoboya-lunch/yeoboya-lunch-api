package com.yeoboya.lunch.api.v1.board.service;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.domain.BoardHashTag;
import com.yeoboya.lunch.api.v1.board.domain.HashTag;
import com.yeoboya.lunch.api.v1.board.repository.BoardRepository;
import com.yeoboya.lunch.api.v1.board.repository.HashTagRepository;
import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.ErrorCode;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        Member member = memberRepository.findByEmail(boardCreate.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Member not found - " + boardCreate.getEmail()));

        String name = Optional.of(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException(""));
        if (!boardCreate.getEmail().equals(name)) {
            return response.fail(ErrorCode.INVALID_AUTH_TOKEN);
        }

        List<BoardHashTag> boardHashTags = new ArrayList<>();
        for (String tag : boardCreate.getHashTag()){
            boolean isHashTag = hashTagRepository.existsHashTagByTag(tag);
            if(isHashTag){
                HashTag findHashTag = hashTagRepository.findHashTagByTag(tag);
                boardHashTags.add(BoardHashTag.createBoardHashTag(findHashTag));
            }else{
                HashTag save = hashTagRepository.save(HashTag.builder().tag(tag).build());
                boardHashTags.add(BoardHashTag.createBoardHashTag(save));
            }
        }

        Board board = Board.createBoard(member, boardCreate, boardHashTags);
        boardRepository.save(board);
        return response.success(Code.SAVE_SUCCESS);
    }
}
