package com.yeoboya.lunch.api.v1.support.domain;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeReadStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_READ_STATUS_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTICE_ID", nullable = false)
    private Notice notice;

    @Column(nullable = false)
    private boolean readStatus;

    @Column(nullable = false)
    private LocalDateTime readAt; // 읽은 시간 추가

    @Builder
    public NoticeReadStatus(Member member, Notice notice, boolean readStatus, LocalDateTime readAt) {
        this.member = member;
        this.notice = notice;
        this.readStatus = readStatus;
        this.readAt = readAt; // 읽은 시간 초기화
    }
}
