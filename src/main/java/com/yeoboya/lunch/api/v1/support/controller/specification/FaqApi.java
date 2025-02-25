package com.yeoboya.lunch.api.v1.support.controller.specification;

import com.yeoboya.lunch.api.v1.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FAQ", description = "자주 묻는 질문 API")
@RequestMapping("/support")
public interface FaqApi {

    @Operation(summary = "FAQ 조회")
    @GetMapping("/faq")
    ResponseEntity<Response.Body> getAllFaqs();
}