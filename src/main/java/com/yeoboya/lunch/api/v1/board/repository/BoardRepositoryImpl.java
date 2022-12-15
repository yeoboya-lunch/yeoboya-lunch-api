package com.yeoboya.lunch.api.v1.board.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.yeoboya.lunch.api.v1.board.domain.QBoard.board;
import static com.yeoboya.lunch.api.v1.board.domain.QBoardHashTag.boardHashTag;
import static com.yeoboya.lunch.api.v1.board.domain.QHashTag.hashTag;
import static com.yeoboya.lunch.api.v1.member.domain.QMember.member;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> boardList(BoardSearch boardSearch, Pageable pageable) {
        return jpaQueryFactory.selectFrom(board)
                .leftJoin(board.boardHashTags, boardHashTag)
                .leftJoin(boardHashTag.hashTag, hashTag)
                .leftJoin(board.member, member)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }
}
