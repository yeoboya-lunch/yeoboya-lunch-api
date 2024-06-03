package com.yeoboya.lunch.api.v1.support.controller;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.support.request.InquiryRequest;
import com.yeoboya.lunch.api.v1.support.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/support")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("/inquiry")
    public ResponseEntity<Response.Body> submitInquiry(@RequestBody InquiryRequest inquiryRequest) {
        return inquiryService.submitInquiry(inquiryRequest);
    }
}
