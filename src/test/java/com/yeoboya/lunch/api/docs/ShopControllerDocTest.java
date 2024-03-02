package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.shop.request.ShopCreate;
import com.yeoboya.lunch.config.SecretsManagerInitializer;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Random;

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
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 4443)
@ExtendWith(RestDocumentationExtension.class)
@WithMockUser(username = "kimhyunjin@outlook.kr", roles = "USER")
@ContextConfiguration(initializers = SecretsManagerInitializer.class)
class ShopControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    private static String uniqueShop;

    @BeforeAll
    static void setUp() {
        uniqueShop = "상점" + new Random().nextInt(10000);
    }

    @Test
    @DisplayName("상점등록")
    void create() throws Exception {
        //given
        ShopCreate request = new ShopCreate();
        request.setShopName(uniqueShop);
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/shop")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("shop/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("shopName").description("가게 이름")
                                        .attributes(key("note").value("중복 안됨"))
                        ),
                        responseFields(
                                fieldWithPath("code")
                                        .type(JsonFieldType.NUMBER)
                                        .description("code"),
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("message"),
                                fieldWithPath("data.shopName")
                                        .type(JsonFieldType.STRING)
                                        .description("등록한 가게 이름")
                                        .attributes(key("length").value("20"))
                                        .attributes(key("note").value("가게 이름 작성중"))

                        )
                ));
    }

    @Test
    @DisplayName("상점목록")
    void shop() throws Exception {
        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("shopName", uniqueShop);
        info.add("page", "0");
        info.add("size", "10");

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
                                fieldWithPath("code")
                                        .type(JsonFieldType.NUMBER)
                                        .description("code"),
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("message"),
                                fieldWithPath("data.pageNo").description("현재 페이지 번호"),
                                fieldWithPath("data.hasPrevious").description("이전 페이지가 있는지 여부"),
                                fieldWithPath("data.hasNext").description("다음 페이지가 있는지 여부"),
                                fieldWithPath("data.isFirst").description("현재 페이지가 첫 페이지인지 여부"),
                                fieldWithPath("data.isLast").description("현재 페이지가 마지막 페이지인지 여부"),
                                fieldWithPath("data.list[].shopName")
                                        .type(JsonFieldType.STRING)
                                        .description("가게이름")
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.[].items[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("아이템이름")
                                        .attributes(key("length").value("20"))
                                        .optional(),
                                fieldWithPath("data.[].items[].price")
                                        .type(JsonFieldType.NUMBER)
                                        .description("가격")
                                        .optional()
                        )
                ));
    }
}
