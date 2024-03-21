package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountCreate;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountEdit;
import com.yeoboya.lunch.api.v1.member.reqeust.MemberInfoEdit;
import com.yeoboya.lunch.config.SecretsManagerInitializer;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.yeoboya-lunch.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@ContextConfiguration(initializers = SecretsManagerInitializer.class)
class MemberControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    @DisplayName("회원리스트")
    void member() throws Exception {
        mockMvc.perform(get("/member"))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("member/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지").optional(),
                                parameterWithName("size").description("사이즈").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.pagination.pageNo").description("현재 페이지 번호"),
                                fieldWithPath("data.pagination.size").description("페이지 사이즈"),
                                fieldWithPath("data.pagination.numberOfElements").description("현재 페이지에 있는 데이터의 수"),
                                fieldWithPath("data.pagination.isFirst").description("현재 페이지가 첫 페이지인지 여부"),
                                fieldWithPath("data.pagination.isLast").description("현재 페이지가 마지막 페이지인지 여부"),
                                fieldWithPath("data.pagination.hasNext").description("다음 페이지가 있는지 여부"),
                                fieldWithPath("data.pagination.hasPrevious").description("이전 페이지가 있는지 여부"),
                                fieldWithPath("data.list[].email").description("이메일 주소"),
                                fieldWithPath("data.list[].name").description("이름"),
                                fieldWithPath("data.list[].nickName").optional().description("닉네임"),
                                fieldWithPath("data.list[].phoneNumber").optional().description("전화번호"),
                                fieldWithPath("data.list[].bio").optional().description("소개"),
                                fieldWithPath("data.list[].account").description("계좌 존재 여부"),
                                fieldWithPath("data.list[].bankName").optional().description("은행 이름"),
                                fieldWithPath("data.list[].accountNumber").optional().description("계좌 번호")
                        )
                ));
    }

    @Test
    @DisplayName("계좌등록")
    @Transactional
    void account() throws Exception {
        //given
        AccountCreate request = AccountCreate.builder()
                .email("account@test.com")
                .bankName("카카오뱅크")
                .accountNumber("3333-01-123456")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/member/account")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("member/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("bankName").description("은행명")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("accountNumber").description("계좌번호")
                                        .type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.bankName").description("은행명")
                                        .type(JsonFieldType.STRING)
                                        .optional(),
                                fieldWithPath("data.accountNumber").description("계좌번호")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("30"))
                                        .optional()
                        )
                ));
    }


    @Test
    @DisplayName("계좌수정")
    void accountUpdate() throws Exception {
        //given
        AccountEdit request = AccountEdit .builder()
                .bankName("토스")
                .accountNumber("010-8349-0706")
                .build();

        String memberEmail = "member@test.com";

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/member/account/{memberEmail}", memberEmail)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("member/account/patch",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberEmail").description("이메일")
                        ),
                        requestFields(
                                fieldWithPath("bankName").description("은행명")
                                        .type(JsonFieldType.STRING)
                                        .optional(),
                                fieldWithPath("accountNumber").description("수정할 계좌번호")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("30"))
                                        .optional()
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
    @DisplayName("회원검색 (profile)")
    public void getMemberProfile() throws Exception {

        String memberEmail = "member@test.com";

        mockMvc.perform(get("/member/{memberEmail}/profile", memberEmail)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member/profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.email").description("이메일")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.name").description("이름")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.nickName").description("닉네임")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.phoneNumber").description("휴대폰")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.bio").description("소개")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.bankName").description("은행")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.accountNumber").description("계좌번호")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.account").description("계좌등록유무")
                                        .type(JsonFieldType.BOOLEAN)
                        )
                ));
    }


    @Test
    @DisplayName("회원정보수정")
    void editMemberInfo() throws Exception {
        // Given
        MemberInfoEdit request = MemberInfoEdit.builder()
                .phoneNumber("010-8349-0706")
                .bio("노력과인내가필요")
                .nickName("테스터")
                .build();

        String memberEmail = "member@test.com";

        String json = objectMapper.writeValueAsString(request);

        // Expected
        mockMvc.perform(patch("/member/setting/info/{memberEmail}", memberEmail)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("member/setting",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberEmail").description("이메일")
                        ),
                        requestFields(
                                fieldWithPath("phoneNumber").description("수정할 전화번호")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("11"))
                                        .optional(),
                                fieldWithPath("bio").description("수정할 설명")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("200"))
                                        .optional(),
                                fieldWithPath("nickName").description("수정할 닉네임")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("20"))
                                        .optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING)
                        )
                ));
    }

}
