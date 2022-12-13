package com.yeoboya.lunch.api.v1.board.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HASHTAG_ID", nullable = false)
    private Long id;

    private String tag;

    @Builder
    public HashTag(String tag){
        this.tag = tag;
    }

}
