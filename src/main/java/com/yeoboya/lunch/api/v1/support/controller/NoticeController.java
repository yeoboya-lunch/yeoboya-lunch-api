package com.yeoboya.lunch.api.v1.support.controller;

import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.support.domain.Notice;
import com.yeoboya.lunch.api.v1.support.request.NoticeRequest;
import com.yeoboya.lunch.api.v1.support.response.NoticeResponseDTO;
import com.yeoboya.lunch.api.v1.support.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/support")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final Response response;


    /**
     * Creates a new notice with the provided notice request.
     *
     * @param noticeRequest The notice request object containing the details of the notice.
     *                      The request object should contain the title, content, category, author,
     *                      priority, startDate, endDate, attachmentUrl, tags, and status fields.
     * @return A ResponseEntity with the success status code and the created notice object.
     */
    @PostMapping("/notices")
    public ResponseEntity<Response.Body> createNotice(@Valid @RequestBody NoticeRequest noticeRequest) {
        Notice notice = noticeService.createNotice(noticeRequest);
        return response.success(Code.SAVE_SUCCESS, notice);
    }

    /**
     * Marks a notice as read for a given notice ID and email.
     *
     * @param noticeId The ID of the notice to mark as read.
     * @param email The email of the user.
     * @return A ResponseEntity with a success status.
     */
    @PostMapping("/notices/{noticeId}/mark-as-read")
    public ResponseEntity<Response.Body> markNoticeAsRead(@PathVariable Long noticeId, @RequestParam String loginId) {
        noticeService.markNoticeAsRead(noticeId, loginId);
        return response.success(Code.SEARCH_SUCCESS);
    }

    /**
     * Retrieves all boards with their read statuses for the given email.
     *
     * @param email The email of the user.
     * @return A ResponseEntity with the list of NoticeResponseDTO objects as the body.
     */
    @GetMapping("/notices")
    public ResponseEntity<Response.Body> getAllBoardsWithReadStatus(@RequestParam String loginId) {
        List<NoticeResponseDTO> notices = noticeService.getAllNoticesWithReadStatus(loginId);
        return response.success(Code.SEARCH_SUCCESS, notices);
    }
}
