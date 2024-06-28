package com.yeoboya.lunch.api.v1.support.service;

import com.yeoboya.lunch.api.v1.support.domain.Inquiry;
import com.yeoboya.lunch.api.v1.support.repository.InquiryRepository;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.support.request.InquiryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final Response response;

    @Transactional
    public ResponseEntity<Response.Body> submitInquiry(InquiryRequest inquiryRequest) {
        Inquiry inquiry = Inquiry.builder()
                .loginId(inquiryRequest.getLoginId())
                .email(inquiryRequest.getEmail())
                .subject(inquiryRequest.getSubject())
                .content(inquiryRequest.getContent())
                .build();
        inquiryRepository.save(inquiry);
        return response.success();
    }
}
