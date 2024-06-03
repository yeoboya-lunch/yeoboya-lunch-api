package com.yeoboya.lunch.api.v1.support.service;

import com.yeoboya.lunch.api.v1.support.domain.Faq;
import com.yeoboya.lunch.api.v1.support.repository.FaqRepository;
import com.yeoboya.lunch.api.v1.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;
    private final Response response;

    @Transactional(readOnly = true)
    public ResponseEntity<Response.Body> getAllFaqs() {
        List<Faq> faqs = faqRepository.findAll();
        return response.success(faqs);
    }
}
