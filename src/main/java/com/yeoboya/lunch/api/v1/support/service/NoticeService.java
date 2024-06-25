package com.yeoboya.lunch.api.v1.support.service;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.api.v1.support.domain.Notice;
import com.yeoboya.lunch.api.v1.support.domain.NoticeReadStatus;
import com.yeoboya.lunch.api.v1.support.repository.NoticeReadStatusRepository;
import com.yeoboya.lunch.api.v1.support.repository.NoticeRepository;
import com.yeoboya.lunch.api.v1.support.request.NoticeRequest;
import com.yeoboya.lunch.api.v1.support.response.NoticeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeReadStatusRepository noticeReadStatusRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Notice createNotice(NoticeRequest noticeRequest) {
        Notice notice = Notice.builder()
                .title(noticeRequest.getTitle())
                .content(noticeRequest.getContent())
                .category(noticeRequest.getCategory())
                .author(noticeRequest.getAuthor())
                .priority(noticeRequest.getPriority())
                .startDate(noticeRequest.getStartDate())
                .endDate(noticeRequest.getEndDate())
                .attachmentUrl(noticeRequest.getAttachmentUrl())
                .viewCount(0) // Initialize view count to 0
                .tags(noticeRequest.getTags())
                .status(noticeRequest.getStatus())
                .build();
        return noticeRepository.save(notice);
    }

    @Transactional
    public void markNoticeAsRead(Long noticeId, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));


        NoticeReadStatus readStatus = noticeReadStatusRepository.findByMemberAndNotice(member, notice)
                .orElseGet(() -> new NoticeReadStatus(member, notice, false, LocalDateTime.now()));

        if (!readStatus.isReadStatus()) {
            readStatus.setReadStatus(true);
            readStatus.setReadAt(LocalDateTime.now());
            notice.setViewCount(notice.getViewCount() + 1); // viewCount 증가
            noticeReadStatusRepository.save(readStatus);
            noticeRepository.save(notice); // viewCount 저장
        } else if (readStatus.getId() == null) {
            // 첫 조회 시 조회수 증가 및 저장
            notice.setViewCount(notice.getViewCount() + 1); // viewCount 증가
            noticeReadStatusRepository.save(readStatus);
            noticeRepository.save(notice); // viewCount 저장
        }
    }


    @Transactional(readOnly = true)
    public List<NoticeResponseDTO> getAllNoticesWithReadStatus(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<Notice> notices = noticeRepository.findAll();
        List<NoticeReadStatus> readStatuses = noticeReadStatusRepository.findByMember(member);

        return notices.stream()
                .map(notice -> {
                    boolean isRead = readStatuses.stream()
                            .anyMatch(readStatus -> readStatus.getNotice().equals(notice) && readStatus.isReadStatus());
                    return NoticeResponseDTO.from(notice, isRead);
                })
                .collect(Collectors.toList());
    }

}
