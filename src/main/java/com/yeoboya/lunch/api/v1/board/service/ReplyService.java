package com.yeoboya.lunch.api.v1.board.service;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.domain.Reply;
import com.yeoboya.lunch.api.v1.board.repository.BoardRepository;
import com.yeoboya.lunch.api.v1.board.repository.ReplyRepository;
import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import com.yeoboya.lunch.api.v1.board.request.ReplyCreateRequest;
import com.yeoboya.lunch.api.v1.board.response.ReplyResponse;
import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Pagination;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final Response response;

    @Transactional
    public ResponseEntity<Response.Body> createReply(ReplyCreateRequest replyCreateRequest) {
        Member member = memberRepository
                .findByEmail(replyCreateRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member not found - " + replyCreateRequest.getEmail()));

        Board board = boardRepository
                .findById(replyCreateRequest.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("Board not found - " + replyCreateRequest.getEmail()));

        Reply parentReply = null;
        if (replyCreateRequest.getParentReplyId() != null) {
            parentReply = replyRepository.findById(replyCreateRequest.getParentReplyId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent reply not found - " + replyCreateRequest.getParentReplyId()));
        }

        Reply reply = Reply.createComment(member, board, replyCreateRequest.getContent(), parentReply);
        replyRepository.save(reply);
        return response.success(Code.SAVE_SUCCESS);
    }

    //게시글(Board)에 대한 특정 댓글(Reply)을 검색합니다.
    public ResponseEntity<Response.Body> fetchBoardReplies(BoardSearch boardSearch, Pageable pageable) {
        Page<Reply> pagedReplies = replyRepository.getReplyForBoard(boardSearch, pageable);

        //이전 단계에서 얻은 페이지네이션된 Reply 객체에서 실제 Reply 객체의 목록을 추출합니다.
        List<Reply> allReplies = pagedReplies.getContent();

        //이 부분에서는 allReplies 목록에서 상위 댓글(부모 댓글이 없는 댓글) 만 걸러내어 parentReplies 목록을 만듭니다.
        List<Reply> parentReplies = allReplies.stream()
                .filter(reply -> reply.getParentReply() == null)
                .collect(Collectors.toList());

        List<ReplyResponse> replyResponses = parentReplies.stream()
                .map(parentReply -> ReplyResponse.of(parentReply.getMember(), parentReply, allReplies))
                .collect(Collectors.toList());

        Pagination pagination = new Pagination(
                pagedReplies.getNumber() + 1,
                pagedReplies.isFirst(),
                pagedReplies.isLast(),
                pagedReplies.isEmpty(),
                pagedReplies.getTotalPages(),
                pagedReplies.getTotalElements());

        Map<String, Object> responseData = Map.of(
                "list", replyResponses,
                "pagination", pagination
        );

        return response.success(Code.SEARCH_SUCCESS, responseData);
    }

    //특정 댓글(Reply)에 대한 대댓글을 검색합니다.
    public ResponseEntity<Response.Body> fetchReplyChildren(BoardSearch boardSearch, Pageable pageable) {
        Page<Reply> pagedReplies = replyRepository.getChildrenForReply(boardSearch, pageable);

        List<Reply> allReplies = pagedReplies.getContent();

        List<ReplyResponse> replyResponses = allReplies.stream()
                .map(reply -> ReplyResponse.of(reply.getMember(), reply, allReplies))
                .collect(Collectors.toList());

        Pagination pagination = new Pagination(
                pagedReplies.getNumber() + 1,
                pagedReplies.isFirst(),
                pagedReplies.isLast(),
                pagedReplies.isEmpty(),
                pagedReplies.getTotalPages(),
                pagedReplies.getTotalElements());


        Map<String, Object> responseData = Map.of(
                "list", replyResponses,
                "pagination", pagination
        );

        return response.success(Code.SEARCH_SUCCESS, responseData);
    }
}
