package com.yeoboya.lunch.config.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
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
        signUp.setAuthority(Authority.ROLE_USER);

        String json = objectMapper.writeValueAsString(signUp);

        //expected
        mockMvc.perform(post("/user/sign-up")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/sign-up",
                        requestFields(
                                fieldWithPath("email").description("이메일")
                                        .attributes(key("constraint").value("메뉴 입력해주세요.")),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("password").description("비밀번호")
                                        .attributes(key("constraint").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")),
                                fieldWithPath("authority").description("권한").ignored()
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
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        )
                ));
    }

    @Test
    void reissue() throws Exception {

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
                        requestFields(
                                fieldWithPath("accessToken").description("엑세스 토큰"),
                                fieldWithPath("refreshToken").description("리프레쉬 토큰")
                        )
                ));
    }

    @Test
    void authority() throws Exception {
    }
}