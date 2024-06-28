package com.yeoboya.lunch.api.v1.board.service;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.domain.Like;
import com.yeoboya.lunch.api.v1.board.repository.BoardRepository;
import com.yeoboya.lunch.api.v1.board.repository.LikeRepository;
import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.ErrorCode;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // Others
    private final Response response;

    @Transactional
    public ResponseEntity<Response.Body> likePost(Long boardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginId(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException("Member not found - " + authentication.getName()));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("No board found with id: " + boardId));

        if (likeRepository.existsByMemberAndBoard(member, board)) {
            return response.fail(ErrorCode.DUPLICATE_RESOURCE, "이미 좋아요를 눌렀습니다");
        }

        Like like = Like.createLike(member, board);
        likeRepository.save(like);
        board.addLike(like);
        return response.success(Code.SAVE_SUCCESS);
    }

    @Transactional
    public ResponseEntity<Response.Body> unlikePost(Long boardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("No board found with id: " + boardId));
        Like like = likeRepository.findByMemberLoginIdAndBoardId(authentication.getName(), boardId)
                .orElseThrow(() -> new EntityNotFoundException("No like found with login/board id: " + authentication.getName() +"/"+ boardId));
        board.removeLike(like);
        likeRepository.delete(like);
        return response.success(Code.SAVE_SUCCESS);
    }

    @Transactional(readOnly = true)
    public boolean hasLiked(String userId, Long boardId) {
        Member member = memberRepository.findByLoginId(userId).orElseThrow(
                () -> new EntityNotFoundException("Member not found - " + userId));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("No board found with id: " + boardId));
        return likeRepository.findByMemberLoginIdAndBoardId(member.getLoginId(), board.getId()).isPresent();
    }
}
