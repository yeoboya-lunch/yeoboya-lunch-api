package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.order.request.OrderRecruitmentCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 4443)
@ExtendWith(RestDocumentationExtension.class)
@WithMockUser(username = "kimhyunjin@outlook.kr", roles = "USER")
@TestPropertySource(properties = "jasypt.encryptor.password=RV47mq6CwLrDEankn8j4")
class RecruitControllerDocTest  {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;


    @Test
    @DisplayName("점심 주문 모집")
    void orderRecruit() throws Exception {
        // 현재 시간에 30분을 추가
        LocalDateTime localDateTimePlus30Mins = LocalDateTime.now().plusMinutes(30);
        Timestamp lastOrderTime = Timestamp.valueOf(localDateTimePlus30Mins);

        //given
        OrderRecruitmentCreate order = OrderRecruitmentCreate.builder()
                .email("khj@gmail.com")
                .title("맥도날드 먹는날")
                .lastOrderTime(lastOrderTime)
                .memo("신상버거 나왔습니다 ㅋㅋ")
                .deliveryFee(2500)
                .shopName("맥도날드")
                .build();

        String json = objectMapper.writeValueAsString(order);

        //expected
        mockMvc.perform(post("/order/recruit")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일").type(JsonFieldType.STRING),
                                fieldWithPath("title").description("모집 제목").type(JsonFieldType.STRING),
                                fieldWithPath("lastOrderTime").description("주문 마감 시간").type(JsonFieldType.STRING),
                                fieldWithPath("memo").description("메모").type(JsonFieldType.STRING),
                                fieldWithPath("deliveryFee").description("배달료").type(JsonFieldType.NUMBER),
                                fieldWithPath("shopName").description("가게 이름").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    @DisplayName("점심 주문 모집 참가")
    void lunchRecruitsGroupJoin() throws Exception {

    }

}
