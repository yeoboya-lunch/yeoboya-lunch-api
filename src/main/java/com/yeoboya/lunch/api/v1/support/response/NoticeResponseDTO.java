package com.yeoboya.lunch.api.v1.support.response;

import com.yeoboya.lunch.api.v1.support.domain.Notice;
import com.yeoboya.lunch.api.v1.support.request.NoticeRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NoticeResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String author;
    private int priority;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String attachmentUrl;
    private int viewCount;
    private String tags;
    private NoticeRequest.NoticeStatus status;
    private boolean isRead;

    public static NoticeResponseDTO from(Notice notice, boolean isRead) {
        return new NoticeResponseDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCategory(),
                notice.getAuthor(),
                notice.getPriority(),
                notice.getStartDate(),
                notice.getEndDate(),
                notice.getAttachmentUrl(),
                notice.getViewCount(),
                notice.getTags(),
                notice.getStatus(),
                isRead
        );
    }
}
