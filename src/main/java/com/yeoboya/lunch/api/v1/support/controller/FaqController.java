package com.yeoboya.lunch.api.v1.support.controller;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.support.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/support")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @GetMapping("/faq")
    public ResponseEntity<Response.Body> getAllFaqs() {
        return faqService.getAllFaqs();
    }
}
