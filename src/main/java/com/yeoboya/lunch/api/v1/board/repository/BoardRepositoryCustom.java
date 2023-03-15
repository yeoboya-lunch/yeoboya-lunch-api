package com.yeoboya.lunch.api.v1.board.repository;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> boardList(BoardSearch boardSearch, Pageable pageable);
}
