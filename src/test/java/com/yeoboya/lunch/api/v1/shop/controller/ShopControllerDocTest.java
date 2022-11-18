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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
class ShopControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void create() throws Exception {
        //given
        ShopCreate request = new ShopCreate();
        request.setShopName("버거킹");
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
                                fieldWithPath("shopName").description("가게 이름")
                                        .attributes(key("note").value("중복 안됨"))
                        ),
                        responseFields(
                                fieldWithPath("shopName")
                                        .type(JsonFieldType.STRING)
                                        .description("등록한 가게 이름")
                                        .attributes(key("length").value("20"))
                                        .attributes(key("note").value("가게 이름 작성중"))

                        )
                ));
    }

    @Test
    void shop() throws Exception {
        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
//        info.add("shopName", "맥도날드");
//        info.add("page", "0");
//        info.add("size", "10");

        //expected
        mockMvc.perform(get("/shop")
                        .params(info))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("shop/get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("shopName").description("검색할 가게이름").optional(),
                                parameterWithName("page").description("페이지").optional(),
                                parameterWithName("size").description("사이즈").optional()
                        ),
                        responseFields(
                                fieldWithPath("[].shopName")
                                        .type(JsonFieldType.STRING)
                                        .description("가게이름")
                                        .attributes(key("length").value("20"))
                                        .attributes(key("note").value("가게 이름 작성중")),
                                fieldWithPath("[].items[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("아이템이름")
                                        .attributes(key("length").value("20"))
                                        .attributes(key("note").value("아이템이름"))
                                        .optional(),
                                fieldWithPath("[].items[].price")
                                        .type(JsonFieldType.NUMBER)
                                        .description("가격")
                                        .attributes(key("length").value("20"))
                                        .attributes(key("note").value("가격"))
                                        .optional()
                        )
                ));
    }
}