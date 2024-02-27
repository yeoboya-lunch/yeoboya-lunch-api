package com.yeoboya.lunch.api.v1.board.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.domain.Reply;
import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yeoboya.lunch.api.v1.board.domain.QReply.reply;


@Repository
public class ReplyRepositoryCustomImpl implements ReplyRepositoryCustom {

    private final JPAQueryFactory query;

    public ReplyRepositoryCustomImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public Page<Reply> replyList(BoardSearch boardSearch, Pageable pageable) {
        List<Reply> content = query.selectFrom(reply)
                .where(reply.board.id.eq(boardSearch.getBoardId()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long totalCount = query
                .select(reply.count())
                .from(reply)
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

}
