package com.yeoboya.lunch.api.v1.board.repository;

import com.yeoboya.lunch.api.v1.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

}
