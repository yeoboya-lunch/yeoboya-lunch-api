package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.support.request.InquiryRequest;
import com.yeoboya.lunch.config.SecretsManagerInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.yeoboya-lunch.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@ContextConfiguration(initializers = SecretsManagerInitializer.class)
public class SupportControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    @DisplayName("문의 작성")
    public void submitInquiry() throws Exception {
        InquiryRequest inquiryRequest = new InquiryRequest();
        inquiryRequest.setEmail("customer@test.com");
        inquiryRequest.setSubject("Issue with order");
        inquiryRequest.setContent("I have an issue with my order.");

        mockMvc.perform(post("/support/inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inquiryRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("support/inquiry",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("고객 이메일")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("subject").description("문의 제목")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("content").description("문의 내용")
                                        .type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("응답 메시지")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data").description("응답 데이터")
                                        .type(JsonFieldType.OBJECT).optional()
                        )
                ));
    }

    @Test
    @DisplayName("FAQ 조회")
    public void getAllFaqs() throws Exception {
        mockMvc.perform(get("/support/faq")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("support/faq",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("응답 코드")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("응답 메시지")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].id").description("FAQ ID")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data[].question").description("질문")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].answer").description("답변")
                                        .type(JsonFieldType.STRING).optional()
                        )
                ));
    }
}
