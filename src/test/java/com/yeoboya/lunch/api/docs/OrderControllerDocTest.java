package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.order.request.OrderCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderItemCreate;
import com.yeoboya.lunch.config.security.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

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
@WithMockCustomUser
class OrderControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser
    @Test
    void order() throws Exception {

        //given
        OrderItemCreate orderItemCreate = new OrderItemCreate();
        orderItemCreate.setItemName("슈비버거");
        orderItemCreate.setOrderQuantity(1);

        OrderItemCreate orderItemCreate2 = new OrderItemCreate();
        orderItemCreate2.setItemName("슈슈버거");
        orderItemCreate2.setOrderQuantity(2);

        List<OrderItemCreate> orderItemCreates = new ArrayList<>();
        orderItemCreates.add(orderItemCreate);
        orderItemCreates.add(orderItemCreate2);

        //given
        OrderCreate order = OrderCreate.builder()
                .email("user@gmail.com")
                .shopName("맥도날드")
                .orderItems(orderItemCreates)
                .build();
        String json = objectMapper.writeValueAsString(order);

        //expected
        mockMvc.perform(post("/order")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("shopName").description("맥도날드")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("orderItems.[].itemName").description("주문아이템")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("orderItems.[].orderQuantity").description("주문수량")
                                        .type(JsonFieldType.NUMBER).optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.orderName").description("주문자명")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("5")),
                                fieldWithPath("data.orderStatus").description("주문상태")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("5"))
                                        .attributes(key("note").value("ORDER, CANCEL")),
                                fieldWithPath("data.totalPrice").description("주문가격")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.orderItems.[].itemName").description("상품명")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.orderItems.[].orderPrice").description("상품가격")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.orderItems.[].orderQuantity").description("주문수량")
                                        .type(JsonFieldType.NUMBER)
                        )
                ));
    }

    @Test
    void getList() throws Exception {

        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
//        info.add("page", "0");
//        info.add("size", "10");

        mockMvc.perform(get("/order/list")
                        .params(info))
                .andExpect(status().isOk())
                .andDo(document("order/get/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("startDate").description("주문 조회 시작 날짜").optional(),
                                parameterWithName("endDate").description("주문 조회 종료 날짜").optional(),
                                parameterWithName("orderName").description("주문자 이름").optional(),
                                parameterWithName("orderStatus").description("주문상태").optional(),
                                parameterWithName("page").description("페이지").optional(),
                                parameterWithName("size").description("사이즈").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.[].orderName").description("주문자명")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("5")),
                                fieldWithPath("data.[].orderStatus").description("주문상태")
                                        .type(JsonFieldType.STRING)
                                        .attributes(key("length").value("5"))
                                        .attributes(key("note").value("ORDER, CANCEL")),
                                fieldWithPath("data.[].totalPrice").description("주문가격")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.[].orderItems.[].itemName").description("상품명")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.[].orderItems.[].orderPrice").description("상품가격")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.[].orderItems.[].orderQuantity").description("주문수량")
                                        .type(JsonFieldType.NUMBER)
                        )
                ));
    }


    @Test
    void cancel() throws Exception {
        //expected
        mockMvc.perform(patch("/order/cancel/{orderId}", 1))
                .andExpect(status().isOk())
                .andDo(document("order/cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문번호")
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