package com.yeoboya.lunch.api.v1.board.domain;

import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.common.domain.BaseEntity;
import com.yeoboya.lunch.api.v1.file.domain.File;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board extends BaseEntity {
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

    private Date createDate;

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<File> files = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<BoardHashTag> boardHashTags = new ArrayList<>();


    public static Board createBoard(Member member, BoardCreate boardCreate, List<BoardHashTag> boardHashTags) {
        Board board = new Board();
        board.setMember(member);
        board.setTitle(boardCreate.getTitle());
        board.setContent(boardCreate.getContent());
        board.setPin(boardCreate.getPin());
        board.setSecret(boardCreate.isSecret());
        board.setCreateDate(new Date());
        for (BoardHashTag boardHashTag : boardHashTags) {
            board.addBoardHashTag(boardHashTag);
        }
        return board;
    }


    public static Board createBoard(Member member, BoardCreate boardCreate, List<BoardHashTag> boardHashTags, File file) {
        Board board = new Board();
        board.setMember(member);
        board.setTitle(boardCreate.getTitle());
        board.setContent(boardCreate.getContent());
        board.setPin(boardCreate.getPin());
        board.setSecret(boardCreate.isSecret());
        board.setCreateDate(new Date());
        for (BoardHashTag boardHashTag : boardHashTags) {
            board.addBoardHashTag(boardHashTag);
        }
        board.addFile(file);
        return board;
    }

    private void addBoardHashTag(BoardHashTag boardHashTag) {
        this.boardHashTags.add(boardHashTag);
        boardHashTag.setBoard(this);
    }

    private void addFile(File file) {
        this.files.add(file);
        if (file.getBoard() != this) {
            file.setBoard(this);
        }
    }
}
