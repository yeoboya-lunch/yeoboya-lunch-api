package com.yeoboya.lunch.api.v1.board.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yeoboya.lunch.api.v1.board.domain.QBoard.board;
import static com.yeoboya.lunch.api.v1.board.domain.QBoardHashTag.boardHashTag;
import static com.yeoboya.lunch.api.v1.board.domain.QHashTag.hashTag;
import static com.yeoboya.lunch.api.v1.member.domain.QMember.member;

@Repository
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory query;

    public BoardRepositoryImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public List<Board> boardList(BoardSearch boardSearch, Pageable pageable) {
        return query.selectFrom(board)
                .leftJoin(board.boardHashTags, boardHashTag)
                .leftJoin(boardHashTag.hashTag, hashTag)
                .leftJoin(board.member, member)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }
}
