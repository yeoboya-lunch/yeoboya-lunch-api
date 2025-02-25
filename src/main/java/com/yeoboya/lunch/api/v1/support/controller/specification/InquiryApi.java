package com.yeoboya.lunch.api.v1.support.controller.specification;

import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.support.request.InquiryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inquiry", description = "문의사항 관리 API")
@RequestMapping("/support")
public interface InquiryApi {

    @Operation(summary = "문의사항 제출")
    @PostMapping("/inquiry")
    ResponseEntity<Response.Body> submitInquiry(@RequestBody InquiryRequest inquiryRequest);
}