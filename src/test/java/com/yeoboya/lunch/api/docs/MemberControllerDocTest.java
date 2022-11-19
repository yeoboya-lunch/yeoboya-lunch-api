package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountCreate;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountEdit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
class MemberControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void member() throws Exception {

        mockMvc.perform(get("/member"))
                .andExpect(status().isOk())
                .andDo(document("member/get/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지").optional(),
                                parameterWithName("size").description("사이즈").optional()
                        ),
                        responseFields(
                                fieldWithPath("[].email").description("아이템 번호")
                                        .type(JsonFieldType.STRING)
                                        .description("이메일")
                                        .attributes(key("length").value("20"))
                                        .attributes(key("note").value("가게 이름 작성중")),
                                fieldWithPath("[].name").description("가게 이름")
                                        .type(JsonFieldType.STRING)
                                        .description("가게이름")
                                        .attributes(key("length").value("20"))
                                        .attributes(key("note").value("가게 이름 작성중")),
                                fieldWithPath("[].account.bankName").description("아이템 번호")
                                        .type(JsonFieldType.STRING)
                                        .description("은행이름")
                                        .attributes(key("length").value("20"))
                                        .attributes(key("note").value("가게 이름 작성중")).optional(),
                                fieldWithPath("[]account.accountNumber").description("아이템 번호")
                                        .type(JsonFieldType.STRING)
                                        .description("게좌번호")
                                        .attributes(key("length").value("20"))
                                        .attributes(key("note").value("가게 이름 작성중")).optional()
                        )
                ));

    }

    @Test
    void account() throws Exception {
        //given
        AccountCreate request = AccountCreate.builder()
                .name("테스터2")
                .bankName("터스")
                .accountNumber("010-8349-0705")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/member/account")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("bankName").description("은행이름").attributes(key("note").value("중복 안됨")),
                                fieldWithPath("accountNumber").description("은행계좌번호")
                        )
                ));
    }

    @Test
    void accountUpdate() throws Exception {
        //given
        AccountEdit request = AccountEdit.builder().bankName("토스").accountNumber("010-8349-0706").build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/member/account/{memberName}", "테스터2")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("member/account/patch",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberName").description("수정할 계좌주인 이름")
                        ),
                        requestFields(
                                fieldWithPath("bankName").description("수정할 은행 이름")
                                        .attributes(key("note").value("메뉴 입력해주세요.")),
                                fieldWithPath("accountNumber").description("수정할 계좌번호")
                        )
                ));
    }
}