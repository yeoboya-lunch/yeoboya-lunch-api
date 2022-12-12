package com.yeoboya.lunch.api.v1.board.domain;


import com.yeoboya.lunch.api.v1.common.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "file_id", nullable = false)
    private Long id;

    //파일원본이름, 파일이름, 파일크기, 파일 확장자, 파일경로, 삭제여부, 작성일
}
