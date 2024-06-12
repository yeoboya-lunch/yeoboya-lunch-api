package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.support.request.InquiryRequest;
import com.yeoboya.lunch.api.v1.support.request.NoticeRequest;
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

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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

    @Test
    @DisplayName("공지사항 생성")
    public void createNotice() throws Exception {
        NoticeRequest noticeRequest = new NoticeRequest();
        noticeRequest.setTitle("New Notice Title");
        noticeRequest.setContent("This is the content of the new notice.");
        noticeRequest.setCategory("General");
        noticeRequest.setAuthor("Admin");
        noticeRequest.setPriority(1);
        noticeRequest.setStartDate(LocalDateTime.now());
        noticeRequest.setEndDate(LocalDateTime.now().plusDays(7));
        noticeRequest.setAttachmentUrl("http://example.com/attachment");
        noticeRequest.setTags("announcement, general");
        noticeRequest.setStatus(NoticeRequest.NoticeStatus.ACTIVE);

        mockMvc.perform(post("/support/notices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticeRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("support/notices/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("공지사항 제목").type(JsonFieldType.STRING),
                                fieldWithPath("content").description("공지사항 내용").type(JsonFieldType.STRING),
                                fieldWithPath("category").description("공지사항 카테고리").type(JsonFieldType.STRING),
                                fieldWithPath("author").description("공지사항 작성자").type(JsonFieldType.STRING),
                                fieldWithPath("priority").description("공지사항 우선순위").type(JsonFieldType.NUMBER),
                                fieldWithPath("startDate").description("공지사항 시작일자").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("endDate").description("공지사항 종료일자").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("attachmentUrl").description("공지사항 첨부 파일의 URL").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("tags").description("공지사항에 대한 키워드 또는 태그").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("status").description("공지사항 상태 (예: 활성, 비활성, 삭제됨)").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("응답 메시지").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.id").description("공지사항 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.title").description("공지사항 제목").type(JsonFieldType.STRING),
                                fieldWithPath("data.content").description("공지사항 내용").type(JsonFieldType.STRING),
                                fieldWithPath("data.category").description("공지사항 카테고리").type(JsonFieldType.STRING),
                                fieldWithPath("data.author").description("공지사항 작성자").type(JsonFieldType.STRING),
                                fieldWithPath("data.priority").description("공지사항 우선순위").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.startDate").description("공지사항 시작일자").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.endDate").description("공지사항 종료일자").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.attachmentUrl").description("공지사항 첨부 파일의 URL").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.viewCount").description("공지사항 조회수").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.tags").description("공지사항에 대한 키워드 또는 태그").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.status").description("공지사항 상태 (예: 활성, 비활성, 삭제됨)").type(JsonFieldType.STRING)
                        )
                ));
    }


    @Test
    @DisplayName("공지사항 읽음 상태 표시")
    public void markNoticeAsRead() throws Exception {
        mockMvc.perform(post("/support/notices/18/mark-as-read")
                        .param("email", "1@1.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("support/notices/mark-as-read",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("email").description("사용자 이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("응답 메시지").type(JsonFieldType.STRING).optional()
                        )
                ));
    }

    @Test
    @DisplayName("공지사항 목록 조회")
    public void getAllNoticesWithReadStatus() throws Exception {
        mockMvc.perform(get("/support/notices")
                        .param("email", "1@1.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("support/notices/get-all-with-read-status",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("email").description("사용자 이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("응답 메시지").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].id").description("공지사항 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].title").description("공지사항 제목").type(JsonFieldType.STRING),
                                fieldWithPath("data[].content").description("공지사항 내용").type(JsonFieldType.STRING),
                                fieldWithPath("data[].category").description("공지사항 카테고리").type(JsonFieldType.STRING),
                                fieldWithPath("data[].author").description("공지사항 작성자").type(JsonFieldType.STRING),
                                fieldWithPath("data[].priority").description("공지사항 우선순위").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].startDate").description("공지사항 시작일자").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].endDate").description("공지사항 종료일자").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].attachmentUrl").description("공지사항에 첨부된 파일의 URL").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].viewCount").description("공지사항 조회수").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].tags").description("공지사항에 대한 키워드 또는 태그").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].status").description("공지사항 상태 (예: 활성, 비활성, 삭제됨)").type(JsonFieldType.STRING),
                                fieldWithPath("data[].read").description("읽음 상태").type(JsonFieldType.BOOLEAN)
                        )
                ));
    }
}
