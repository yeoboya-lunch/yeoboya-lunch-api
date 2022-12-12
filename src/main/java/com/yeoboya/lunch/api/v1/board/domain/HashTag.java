package com.yeoboya.lunch.api.v1.board.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HASHTAG_ID", nullable = false)
    private Long id;

    @Column
    private String value;

}
