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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
class ItemControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void create() throws Exception {
        //given
        ItemCreate request = ItemCreate.builder()
                .shopName("맥도날드")
                .itemName("더블불고기버거")
                .price(7300)
                .build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/item")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("item/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("shopName").description("식당이름"),
                                fieldWithPath("itemName").description("메뉴이름")
                                        .attributes(key("constraint").value("메뉴 입력해주세요.")),
                                fieldWithPath("price").description("가격")
                        )
                ));
    }

    @Test
    void getItem() throws Exception {

        mockMvc.perform(get("/item/{itemId}", 1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("item/get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("itemId").description("아이템 번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("아이템 번호").ignored(),
                                fieldWithPath("name").description("아이템 제목"),
                                fieldWithPath("price").description("아이템 가격"),
                                fieldWithPath("shopName").description("상점 이름")
                        )
                ));
    }

    @Test
    @Transactional
    void updateItem() throws Exception {

        //given
        ItemEdit request = ItemEdit.builder().name("NEW 슈비버거").price(10500).build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/item/{itemId}", 1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document("item/patch",
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

        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("page", "0");
        info.add("size", "10");

        mockMvc.perform(get("/item")
                        .params(info))
                .andExpect(status().isOk())
                .andDo(document("item/get/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지").optional(),
                                parameterWithName("size").description("사이즈").optional()
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("아이템 번호").ignored(),
                                fieldWithPath("[].shopName").description("가게 이름").ignored(),
                                fieldWithPath("[].name").description("아이템 제목").ignored(),
                                fieldWithPath("[].price").description("아이템 가격").ignored()
                        )
                ));
    }

    @Test
    void deleteItem() throws Exception {
        mockMvc.perform(delete("/item/{itemId}", 4)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("item/delete"));
    }
}