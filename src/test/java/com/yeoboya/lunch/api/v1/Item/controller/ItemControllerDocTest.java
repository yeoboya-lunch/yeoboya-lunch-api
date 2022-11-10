package com.yeoboya.lunch.api.v1.Item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
class ItemControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void create() throws Exception {
        //given
        ItemCreate request = ItemCreate.builder().
                itemName("챙길밥").
                price(6300).
                build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/item")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("메뉴이름")
                                        .attributes(key("constraint").value("메뉴 입력해주세요.")),
                                fieldWithPath("price").description("가격")
                        )
                ));
    }

    @Test
    void getItem() throws Exception {

        mockMvc.perform(get("/item/{itemId}", 13)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("itemId").description("아이템 번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("아이템 번호"),
                                fieldWithPath("name").description("아이템 제목"),
                                fieldWithPath("price").description("아이템 가격")
                        )
                ));

    }

    @Test
    @Transactional
    void updateItem() throws Exception {
        //given
        ItemEdit request = ItemEdit.builder().name("수정밥").price(1234).build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/item/{itemId}", 2)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("updateItem",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("itemId").description("수정할 아이템 번호")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 메뉴 이름")
                                        .attributes(key("constraint").value("메뉴 입력해주세요.")),
                                fieldWithPath("price").description("수정할 가격")
                        )
                ));
    }

    @Test
    void getList() throws Exception {
        mockMvc.perform(get("/item?page=1&size=10")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("아이템 번호"),
                                fieldWithPath("[].name").description("아이템 제목"),
                                fieldWithPath("[].price").description("아이템 가격")
                        )
                ));
    }
}