package com.yeoboya.lunch.api.v1.support.domain;

import com.yeoboya.lunch.api.v1.common.domain.BaseEntity;
import com.yeoboya.lunch.api.v1.support.request.NoticeRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID", nullable = false)
    private Long id; // 공지사항 ID

    @Column(nullable = false)
    private String title; // 공지사항 제목

    @Column(nullable = false)
    private String content; // 공지사항 내용

    @Column(nullable = false)
    private String category; // 공지사항 카테고리 (예: 일반, 시스템, 이벤트 등)

    @Column(nullable = false)
    private String author; // 공지사항 작성자

    @Column(nullable = false)
    private int priority; // 공지사항 우선순위 (0 = 낮음, 1 = 보통, 2 = 높음)

    @Column(nullable = true)
    private LocalDateTime startDate; // 공지사항 시작일자

    @Column(nullable = true)
    private LocalDateTime endDate; // 공지사항 종료일자

    @Column(nullable = true)
    private String attachmentUrl; // 공지사항에 첨부된 파일의 URL

    @Column(nullable = false)
    private int viewCount; // 공지사항 조회수

    @Column(nullable = true)
    private String tags; // 공지사항에 대한 키워드 또는 태그

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeRequest.NoticeStatus status; // 공지사항 상태 (예: 활성, 비활성, 삭제됨)


    @Builder
    public Notice(String title, String content, String category, String author, int priority, LocalDateTime startDate,
                  LocalDateTime endDate, String attachmentUrl, int viewCount, String tags, NoticeRequest.NoticeStatus status) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attachmentUrl = attachmentUrl;
        this.viewCount = viewCount;
        this.tags = tags;
        this.status = status;
    }
}
