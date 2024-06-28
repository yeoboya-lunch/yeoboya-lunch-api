package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import com.yeoboya.lunch.api.v1.shop.domain.Shop;
import com.yeoboya.lunch.api.v1.shop.service.ShopService;
import com.yeoboya.lunch.config.SecretsManagerInitializer;
import com.yeoboya.lunch.config.TestUtil;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Random;

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
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.yeoboya-lunch.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@ContextConfiguration(initializers = SecretsManagerInitializer.class)
class ItemControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ShopService shopService;

    private TestUtil testUtil;
    private static int unique;

    @BeforeEach
    void setUp() {
        testUtil = new TestUtil(mockMvc, objectMapper);
        unique = new Random().nextInt(10000);
    }


    @Test
    @DisplayName("아이템 등록")
    @WithMockUser(username = "admin", roles = "USER")
    @Transactional
    void create() throws Exception {

        Shop shop = shopService.selectShop();

        //given
        ItemCreate request = ItemCreate.builder()
                .shopName(shop.getName())
                .itemName("아이템"+unique)
                .price(unique)
                .build();
        String json = objectMapper.writeValueAsString(request);

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        //expected
        mockMvc.perform(post("/item")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
                        .with(postProcessor)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("item/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("shopName").description("식당이름"),
                                fieldWithPath("itemName").description("메뉴이름").attributes(key("note").value("중복 안됨")),
                                fieldWithPath("price").description("가격")
                        ),
                        responseFields(
                                fieldWithPath("code")
                                        .type(JsonFieldType.NUMBER)
                                        .description("code"),
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("message"),
                                fieldWithPath("data.itemId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("아이템번호")
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.shopName")
                                        .type(JsonFieldType.STRING)
                                        .description("가게이름")
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.name")
                                        .type(JsonFieldType.STRING)
                                        .description("상품이름")
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.price")
                                        .type(JsonFieldType.NUMBER)
                                        .description("상품가격")
                                        .attributes(key("length").value("20"))
                        )
                ));
    }

    @Test
    @DisplayName("아이템 단건 조회")
    void getItem() throws Exception {

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        mockMvc.perform(get("/item/{itemId}", 1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .with(postProcessor)
                )
                .andExpect(status().isOk())
                .andDo(document("item/get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("itemId").description("상품번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.shopName").description("상점명")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.name").description("상품명")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.price").description("상품가격")
                                        .type(JsonFieldType.NUMBER)
                        )
                ));
    }


    @Test
    @DisplayName("아이템 리스트 조회")
    @Disabled
    void getList() throws Exception {
        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("page", "0");
        info.add("size", "10");

        String shopName = "NIKE";

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        mockMvc.perform(get("/item/shop/{shopName}", shopName)
                        .params(info)
                        .with(postProcessor)
                )
                .andExpect(status().isOk())
                .andDo(document("item/shop/get/list",
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
                                fieldWithPath("data.[].shopName").description("상점명")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.[].name").description("상품명")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("20")),
                                fieldWithPath("data.[].price").description("상품가격")
                                        .type(JsonFieldType.NUMBER)
                                        .attributes(key("length").value("20"))
                        )
                ));
    }


    @Test
    @Transactional
    @DisplayName("아이템 업데이트")
    void updateItem() throws Exception {

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        //given
        ItemEdit request = ItemEdit.builder().name("NEW 슈비버거").price(6800).build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/item/{itemId}", 1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
                        .with(postProcessor)
                )
                .andExpect(status().isOk())
                .andDo(document("item/patch",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("itemId").description("상품번호")
                        ),
                        requestFields(
                                fieldWithPath("name").description("상품명")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("price").description("상품가격")
                                        .type(JsonFieldType.NUMBER)
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
    @Transactional
    @DisplayName("아이템 삭제")
    void deleteItem() throws Exception {
        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        mockMvc.perform(delete("/item/{itemId}", 1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .with(postProcessor)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("item/delete",
                        pathParameters(
                                parameterWithName("itemId").description("상품번호")
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
