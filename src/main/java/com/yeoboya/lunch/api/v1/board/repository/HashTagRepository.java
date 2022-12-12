package com.yeoboya.lunch.api.v1.board.repository;

import com.yeoboya.lunch.api.v1.board.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {


    Optional<HashTag> findHashTagByValue(String value);
}
