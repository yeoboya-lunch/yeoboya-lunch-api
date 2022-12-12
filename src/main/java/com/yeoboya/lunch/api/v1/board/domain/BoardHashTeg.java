package com.yeoboya.lunch.api.v1.board.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints =
                @UniqueConstraint(
                        name = "UK_HASHTAG_ROLE",
                        columnNames = {"BOARD_ID", "HASHTAG_ID"}
                )
)
public class BoardHashTeg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_HASHTAG_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HASHTAG_ID")
    private HashTag hashTag;

}
