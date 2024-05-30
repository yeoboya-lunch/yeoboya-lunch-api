package com.yeoboya.lunch.api.v1.board.repository;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.domain.Like;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberEmailAndBoardId(String email, Long boardId);
    boolean existsByMemberAndBoard(Member member, Board board);
}
