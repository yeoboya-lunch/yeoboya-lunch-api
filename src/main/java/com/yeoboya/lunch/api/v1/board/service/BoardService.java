package com.yeoboya.lunch.api.v1.board.service;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.domain.BoardHashTag;
import com.yeoboya.lunch.api.v1.board.domain.HashTag;
import com.yeoboya.lunch.api.v1.board.repository.BoardRepository;
import com.yeoboya.lunch.api.v1.board.repository.HashTagRepository;
import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import com.yeoboya.lunch.api.v1.board.request.FileBoardCreate;
import com.yeoboya.lunch.api.v1.board.response.BoardResponse;
import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.ErrorCode;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.file.domain.File;
import com.yeoboya.lunch.api.v1.file.repository.FileRepository;
import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import com.yeoboya.lunch.api.v1.file.service.FileService;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final HashTagRepository hashTagRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final Response response;


    public ResponseEntity<Body> saveBoard(BoardCreate boardCreate) {
        Member member = memberRepository.findByEmail(boardCreate.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Member not found - " + boardCreate.getEmail()));

        String name = Optional.of(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException(""));
        if (!boardCreate.getEmail().equals(name)) {
            return response.fail(ErrorCode.INVALID_AUTH_TOKEN);
        }

        List<BoardHashTag> boardHashTags = new ArrayList<>();
        if (boardCreate.getHashTag() != null) {
            for (String tag : boardCreate.getHashTag()) {
                boolean isHashTag = hashTagRepository.existsHashTagByTag(tag);
                if (isHashTag) {
                    HashTag findHashTag = hashTagRepository.findHashTagByTag(tag);
                    boardHashTags.add(BoardHashTag.createBoardHashTag(findHashTag));
                } else {
                    HashTag save = hashTagRepository.save(HashTag.builder().tag(tag).build());
                    boardHashTags.add(BoardHashTag.createBoardHashTag(save));
                }
            }
        }

        Board board = Board.createBoard(member, boardCreate, boardHashTags);
        boardRepository.save(board);
        return response.success(Code.SAVE_SUCCESS);
    }


    public ResponseEntity<Body> saveBoardPhoto(MultipartFile file, FileBoardCreate fileBoardCreate) {
        Member member = memberRepository.findByEmail(fileBoardCreate.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Member not found - " + fileBoardCreate.getEmail()));

        String name = Optional.of(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException(""));
        if (!fileBoardCreate.getEmail().equals(name)) {
            return response.fail(ErrorCode.INVALID_AUTH_TOKEN);
        }

        List<BoardHashTag> boardHashTags = new ArrayList<>();
        if (fileBoardCreate.getHashTag() != null) {
            for (String tag : fileBoardCreate.getHashTag()) {
                boolean isHashTag = hashTagRepository.existsHashTagByTag(tag);
                if (isHashTag) {
                    HashTag findHashTag = hashTagRepository.findHashTagByTag(tag);
                    boardHashTags.add(BoardHashTag.createBoardHashTag(findHashTag));
                } else {
                    HashTag save = hashTagRepository.save(HashTag.builder().tag(tag).build());
                    boardHashTags.add(BoardHashTag.createBoardHashTag(save));
                }
            }
        }

        FileUploadResponse upload = fileService.upload(file, fileBoardCreate.getUploadType());
        File fileBuild = File.builder().fileUploadResponse(upload).build();

        Board board = Board.createBoard(member, fileBoardCreate, boardHashTags, fileBuild);
        boardRepository.save(board);
        return response.success(Code.SAVE_SUCCESS);
    }

    public ResponseEntity<Body> list(BoardSearch boardSearch, Pageable pageable) {
        List<BoardResponse> collect = boardRepository.boardList(boardSearch, pageable)
                .stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
        return response.success(Code.SEARCH_SUCCESS, collect);
    }

}
