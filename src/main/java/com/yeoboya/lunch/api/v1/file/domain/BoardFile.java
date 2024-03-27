package com.yeoboya.lunch.api.v1.file.domain;


import com.yeoboya.lunch.api.v1.board.domain.Board;
import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_FILE_ID", nullable = false)
    private Long id;

    private String originalFileName;

    private String fileName;

    private String filePath;

    private String extension;

    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Builder
    public BoardFile(Board board, FileUploadResponse fileUploadResponse) {
        this.board = board;
        this.originalFileName = fileUploadResponse.getOriginalFileName();
        this.fileName = fileUploadResponse.getFileName();
        this.filePath = fileUploadResponse.getFilePath();
        this.extension = fileUploadResponse.getExtension();
        this.size = fileUploadResponse.getSize();
    }

    public void serBoard(Board board) {
        this.board = board;
        if (!board.getBoardFiles().contains(this)) {
            board.getBoardFiles().add(this);
        }
    }
}
