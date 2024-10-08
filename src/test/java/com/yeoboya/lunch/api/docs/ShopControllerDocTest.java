package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.shop.request.ShopAndItemCreate;
import com.yeoboya.lunch.api.v1.shop.request.ShopCreate;
import com.yeoboya.lunch.config.SecretsManagerInitializer;
import com.yeoboya.lunch.config.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
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
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.yeoboya-lunch.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@ContextConfiguration(initializers = SecretsManagerInitializer.class)
class ShopControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    private TestUtil testUtil;

    private static String uniqueShop;

    @BeforeEach
    void setUp() {
        uniqueShop = "상점" + new Random().nextInt(10000);
        testUtil = new TestUtil(mockMvc, objectMapper);
    }

    @Test
    @DisplayName("상점등록")
    void create() throws Exception {
        //given
        ShopCreate request = new ShopCreate();
        request.setShopName(uniqueShop);
        String json = objectMapper.writeValueAsString(request);

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        //expected
        mockMvc.perform(post("/shop")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
                        .with(postProcessor)
                )
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

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        //expected
        mockMvc.perform(get("/shop")
                        .params(info)
                        .with(postProcessor)
                )
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
                                        fieldWithPath("data.pagination.pageNo").description("페이지 번호"),
                                        fieldWithPath("data.pagination.size").description("페이지 사이즈"),
                                        fieldWithPath("data.pagination.numberOfElements").description("현재 페이지의 엘리먼트 갯수"),
                                        fieldWithPath("data.pagination.isFirst").description("현재 페이지가 첫 페이지인지 여부"),
                                        fieldWithPath("data.pagination.isLast").description("현재 페이지가 마지막 페이지인지 여부"),
                                        fieldWithPath("data.pagination.hasNext").description("다음 페이지가 있는지 여부"),
                                        fieldWithPath("data.pagination.hasPrevious").description("이전 페이지가 있는지 여부"),
                                        fieldWithPath("data.list[]").description("상점").optional(),
                                        fieldWithPath("data.list[].shopName")
                                                .type(JsonFieldType.STRING)
                                                .description("가게이름")
                                                .attributes(key("length").value("20")),
                                        fieldWithPath("data.list[].items[].name")
                                                .type(JsonFieldType.STRING)
                                                .description("아이템이름")
                                                .attributes(key("length").value("20"))
                                                .optional(),
                                        fieldWithPath("data.list[].items[].price")
                                                .type(JsonFieldType.NUMBER)
                                                .description("가격")
                                                .optional())
                        )
                );
    }

    @Test
    @DisplayName("상점 생성 및 아이템 추가")
    void createShopAndAddItem() throws Exception {
        Random rand = new Random();

        // given
        ShopAndItemCreate create = new ShopAndItemCreate();
        create.setShopName(uniqueShop);
        create.setItems(Arrays.asList(
                ItemCreate.builder().itemName("Apple").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Banana").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Cherry").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Date").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Elderberry").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Fig").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Grape").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Honeydew").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Italian Lime").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Jackfruit").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Kiwi").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Lemon").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Melon").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Nectarine").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Orange").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Peach").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Quince").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Raspberry").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Strawberry").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Tangerine").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Ugli Fruit").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Vitamin C").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Watermelon").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Xigua").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Yellow Passionfruit").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Zucchini").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Fruit26").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Fruit27").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Fruit28").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Fruit29").price(rand.nextInt(50000) + 1000).build(),
                ItemCreate.builder().itemName("Fruit30").price(rand.nextInt(50000) + 1000).build()
        ));

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        // when/then
        mockMvc.perform(post("/shop/create")
                        .content(objectMapper.writeValueAsString(create))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(postProcessor)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("shop/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("shopName").description("상점 이름"),
                                fieldWithPath("items").description("아이템 목록"),
                                fieldWithPath("items[].shopName").description("상점 이름").optional(),
                                fieldWithPath("items[].itemName").description("아이템 이름"),
                                fieldWithPath("items[].price").description("아이템 가격")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("code"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("message"),
                                fieldWithPath("data[].shopName").type(JsonFieldType.STRING).description("상점 이름"),
                                fieldWithPath("data[].itemId").type(JsonFieldType.NUMBER).description("아이템 ID"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("아이템 이름"),
                                fieldWithPath("data[].price").type(JsonFieldType.NUMBER).description("아이템 가격")
                        )
                ));
    }
}
