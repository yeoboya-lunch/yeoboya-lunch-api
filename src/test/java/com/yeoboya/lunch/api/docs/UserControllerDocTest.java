package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.config.security.WithMockCustomUser;
import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import org.junit.jupiter.api.Disabled;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@WithMockCustomUser
class UserControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
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
                .andExpect(status().isOk())
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
    void login() throws Exception {
        //given
        UserRequest.SignIn signIn = new UserRequest.SignIn();
        signIn.setEmail("khjzzm@gmail.com");
        signIn.setPassword("1234qwer!@#$");

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

    @Test
    @Disabled
    void reissue() throws Exception {

    }

    @Test
    void changePassword() throws Exception {
        //given
        UserRequest.Credentials credentials = new UserRequest.Credentials();
        credentials.setEmail("khjzzm@gmail.com");
        credentials.setOldPassword("1234qwer!@#$");
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
                                fieldWithPath("confirmNewPassword").description("비밀번호 확인")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING)
                        )
                ));
    }

    //fixme
    @Disabled
    @Test
    void logout() throws Exception {
        //given
        UserRequest.SignOut signOut = new UserRequest.SignOut();
        signOut.setAccessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraGpAZ21haWwuY29tIiwianRpIjoiZmNkYjc3MDUtZmYwMC00YTFlLThmNTktNTUwNjk3MDMzNjRlIiwiaXNzIjoieWVvYm95YSIsImlhdCI6MTY2ODY3NDEyMywiZXhwIjoxNjY4Njc3NzIzLCJhdXRoIjoiUk9MRV9VU0VSIn0.pH_7Dq26U3hneBI0PkfLndTpzzo_Yd3NpgFQRGyWE7o");
        signOut.setRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NjkyNzg5MjN9.7Qd_YJIpISd1wec7_SAynikH2OdXgIqp9ivLkQyqUtM");

        String json = objectMapper.writeValueAsString(signOut);

        //expected
        mockMvc.perform(post("/user/sign-out")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/sign-out",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("accessToken").description("엑세스 토큰"),
                                fieldWithPath("refreshToken").description("리프레쉬 토큰")
                        )
                ));
    }

    @Test
    @Disabled
    void authority() throws Exception {
    }
}