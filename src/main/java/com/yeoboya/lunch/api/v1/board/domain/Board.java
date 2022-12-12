package com.yeoboya.lunch.api.v1.board.domain;

import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "board")
public class Board {
    //필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member.class)
    @JoinColumn(name = "MEMBER_ID", updatable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<BoardHashTeg> boardHashTags = new ArrayList<>();

    public static Board createBoard(Member member, BoardCreate boardCreate)
}