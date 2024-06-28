package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.review.request.ReviewRequest;
import com.yeoboya.lunch.api.v1.review.request.ReviewUpdateRequest;
import com.yeoboya.lunch.config.SecretsManagerInitializer;
import com.yeoboya.lunch.config.TestUtil;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.yeoboya-lunch.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@ContextConfiguration(initializers = SecretsManagerInitializer.class)
public class ReviewControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    private TestUtil testUtil;

    @BeforeEach
    void setUp() {
        testUtil = new TestUtil(mockMvc, objectMapper);
    }

    @Test
    @DisplayName("리뷰 추가")
    @Transactional
    public void addReview() throws Exception {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setOrderId(2L);
        reviewRequest.setContent("Great food!");
        reviewRequest.setShopRating(5);

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(postProcessor)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("review/add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderId").description("주문 ID")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("content").description("리뷰 내용")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("shopRating").description("상점 평점")
                                        .type(JsonFieldType.NUMBER)
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("응답 메시지")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data").description("응답 데이터")
                                        .type(JsonFieldType.OBJECT).optional()
                        )
                ));
    }

    @Test
    @DisplayName("리뷰 수정")
    public void updateReview() throws Exception {
        ReviewUpdateRequest reviewRequest = new ReviewUpdateRequest();
        reviewRequest.setContent("Updated review content");
        reviewRequest.setShopRating(4);

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        mockMvc.perform(put("/reviews/{reviewId}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(postProcessor)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("review/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").description("리뷰 내용").type(JsonFieldType.STRING),
                                fieldWithPath("shopRating").description("상점 평점").type(JsonFieldType.NUMBER)
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("data").description("응답 데이터").type(JsonFieldType.OBJECT).optional()
                        )
                ));
    }

    @Test
    @DisplayName("리뷰 삭제")
    @Transactional
    public void deleteReview() throws Exception {

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        mockMvc.perform(delete("/reviews/{reviewId}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(postProcessor)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("review/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("응답 메시지")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data").description("응답 데이터")
                                        .type(JsonFieldType.OBJECT).optional()
                        )
                ));
    }

    @Test
    @DisplayName("리뷰 통계 조회")
    public void getReviewStatistics() throws Exception {

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        mockMvc.perform(get("/reviews/stats/{shopId}", 302L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(postProcessor)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("review/stats",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("shopId").description("상점 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.monthlyStats[].year").description("연도")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.monthlyStats[].month").description("월")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.monthlyStats[].averageRating").description("월별 평균 평점")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.overallAverageRating").description("전체 평균 평점")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.ratingDistribution[].rating").description("평점")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.ratingDistribution[].count").description("평점별 리뷰 개수")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.totalReviewCount").description("총 리뷰 개수")
                                        .type(JsonFieldType.NUMBER)
                        )
                ));
    }

    @Test
    @DisplayName("정렬된 리뷰 조회")
    public void getSortedReviews() throws Exception {
        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        mockMvc.perform(get("/reviews/{shopId}/sorted", 302L)
                        .param("sortBy", "shopRating")
                        .param("order", "asc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(postProcessor)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("review/sorted",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("shopId").description("상점 ID")
                        ),
                        requestParameters(
                                parameterWithName("sortBy").description("정렬 기준 (예: shopRating, createdDate)"),
                                parameterWithName("order").description("정렬 순서 (asc 또는 desc)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.shopName").description("상점 이름")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.reviews[].reviewId").description("리뷰 ID")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.reviews[].orderId").description("주문 ID")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.reviews[].content").description("리뷰 내용")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.reviews[].shopRating").description("상점 평점")
                                        .type(JsonFieldType.NUMBER)
                        )
                ));
    }
}
