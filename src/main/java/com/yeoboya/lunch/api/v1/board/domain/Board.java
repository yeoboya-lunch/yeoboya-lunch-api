package com.yeoboya.lunch.api.v1.board.domain;

import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private int pin;

    private boolean secret;

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<BoardHashTag> boardHashTags = new ArrayList<>();


    public static Board createBoard(Member member, BoardCreate boardCreate, List<BoardHashTag> boardHashTags){
        Board board = new Board();
        board.setMember(member);
        board.setTitle(boardCreate.getTitle());
        board.setContent(boardCreate.getContent());
        board.setPin(boardCreate.getPin());
        board.setSecret(boardCreate.isSecret());
        for(BoardHashTag boardHashTag : boardHashTags){
            board.addBoardHashTag(boardHashTag);
        }
        return board;
    }

    private void addBoardHashTag(BoardHashTag boardHashTag) {
        this.boardHashTags.add(boardHashTag);
        boardHashTag.setBoard(this);
    }
}