package com.yeoboya.lunch.api.v1.support.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeRequest {
    private String title;   // 공지사항 제목
    private String content; // 공지사항 내용
    private String category; // 공지사항 카테고리
    private String author; // 공지사항 작성자
    private int priority; // 공지사항 우선순위 (0 = 낮음, 1 = 보통, 2 = 높음)
    private LocalDateTime startDate; // 공지사항 시작일자
    private LocalDateTime endDate; // 공지사항 종료일자
    private String attachmentUrl; // 공지사항에 첨부된 파일의 URL
    private String tags; // 공지사항에 대한 키워드 또는 태그
    private NoticeStatus status; // 공지사항 상태 (예: 활성, 비활성, 삭제됨)

    public enum NoticeStatus {
        ACTIVE, // 활성
        INACTIVE, // 비활성
        DELETED // 삭제됨
    }
}
