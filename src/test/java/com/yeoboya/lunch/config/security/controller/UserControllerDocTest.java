package com.yeoboya.lunch.config.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.config.security.dto.reqeust.UserRequest;
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
        signUp.setEmail("kkkkkk@gmail.com");
        signUp.setName("김현진");
        signUp.setPassword("1234qwer@@");

        String json = objectMapper.writeValueAsString(signUp);

        //expected
        mockMvc.perform(post("/member/sign-up")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member",
                        requestFields(
                                fieldWithPath("id").description("아이디"),
                                fieldWithPath("email").description("이메일")
                                        .attributes(key("constraint").value("메뉴 입력해주세요.")),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("password").description("비밀번호").optional()
                        )
                ));
    }

    @Test
    void login() {
    }

    @Test
    void reissue() {
    }

    @Test
    void logout() {
    }

    @Test
    void authority() {
    }
}