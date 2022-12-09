package com.yeoboya.lunch.api.docs;

import com.yeoboya.lunch.api.container.ContainerDI;
import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@WithMockUser(username = "kimhyunjin@outlook.kr", roles = "USER")
class UserControllerDocTest extends ContainerDI {

    @Test
    @DisplayName("회원가입")
    void signUp() throws Exception {
        //given
        UserRequest.SignUp signUp = new UserRequest.SignUp();
        signUp.setEmail("khjzzm@gmail.com");
        signUp.setName("김현진");
        signUp.setPassword("1234qwer!@#$");

        String json = objectMapper.writeValueAsString(signUp);

        //expected
        mockMvc.perform(post("/user/sign-up")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("user/sign-up",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("password").description("비밀번호")
                                        .attributes(key("note").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
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
    @Disabled
    @DisplayName("패스워드 변경")
    void changePassword() throws Exception {
        //given
        UserRequest.Credentials credentials = new UserRequest.Credentials();
        credentials.setEmail("khj@gmail.com");
        credentials.setOldPassword("test5678((");
        credentials.setNewPassword("qwer1234@@");
        credentials.setConfirmNewPassword("qwer1234@@");

        String json = objectMapper.writeValueAsString(credentials);

        //expected
        mockMvc.perform(patch("/user/setting/security")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/setting/security",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("oldPassword").description("전 비밀번호"),
                                fieldWithPath("newPassword").description("새로운 비밀번호"),
                                fieldWithPath("confirmNewPassword").description("비밀번호 확인"),
                                fieldWithPath("passKey").description("KnowPassKey").ignored()
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
    @Disabled
    @DisplayName("로그인")
    void login() throws Exception {
        //given
        UserRequest.SignIn signIn = new UserRequest.SignIn();
        signIn.setEmail("khj@gmail.com");
        signIn.setPassword("qwer1234@@");

        String json = objectMapper.writeValueAsString(signIn);

        //expected
        mockMvc.perform(post("/user/sign-in")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/sign-in",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.subject").description("이메일")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.id").description("고유번호")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.issuer").description("발행자")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.issueDAt").description("발행일자")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.accessToken").description("토큰")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.refreshToken").description("리프레시 토큰")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.tokenExpirationTime").description("토큰 만료기간")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.refreshTokenExpirationTime").description("리프레시 토큰 만료기간")
                                        .type(JsonFieldType.NUMBER)

                        )
                ));
    }



    //todo
    //로그아웃, 토큰 재발급, 비밀번호 변경 이메일전송, 비밀번호 초기화
}