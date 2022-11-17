package com.yeoboya.lunch.api.v1.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.shop.request.ShopCreate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void create() throws Exception {
        //given
        ShopCreate request = new ShopCreate();
        request.setShopName("맥도날드");
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/shop")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("shop/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("shopName").description("가게 이름").attributes(key("constraint").value("가게이름을 입력해주세요."))
                        )
                ));
    }

    @Test
    void shop() throws Exception {
        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("shopName", "맥도날드");

        //expected
        mockMvc.perform(get("/shop")
                        .params(info))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("shop/get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("shopName").description("가게 이름").optional(),
                                fieldWithPath("page").description("페이지").optional(),
                                fieldWithPath("size").description("사이즈").optional()
                        )
                ));
    }
}