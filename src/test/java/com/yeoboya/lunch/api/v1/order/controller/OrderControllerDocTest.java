package com.yeoboya.lunch.api.v1.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.order.request.OrderCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderItemCreate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
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
                .email("tester@gmail.com")
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
                .andExpect(status().isOk())
                .andDo(document("order/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일")
                                        .attributes(key("optional").value("N")),
                                fieldWithPath("shopName").description("맥도날드")
                                        .attributes(key("optional").value("N")),
                                fieldWithPath("orderItems.[].itemName").description("주문아이템")
                                        .attributes(key("optional").value("N")),
                                fieldWithPath("orderItems.[].orderQuantity").description("주문수량")
                                        .attributes(key("optional").value("N"))
                        )
                ));
    }

    @Test
    void getList() throws Exception {

        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("page", "0");
        info.add("size", "10");

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
                                fieldWithPath("[].id").description("주문 번호").attributes(key("constraint").value("가게이름을 입력해주세요.")),
                                fieldWithPath("[].orderName").description("주문자 이름").attributes(key("constraint").value("가게이름을 입력해주세요.")),
                                fieldWithPath("[].totalPrice").description("주문 총 가격").attributes(key("constraint").value("가게이름을 입력해주세요.")),
                                fieldWithPath("[].orderItems.[].itemName").description("주문 아이템 이름").attributes(key("constraint").value("가게이름을 입력해주세요.")),
                                fieldWithPath("[].orderItems.[].orderPrice").description("주문 아이템 가격").attributes(key("constraint").value("가게이름을 입력해주세요.")),
                                fieldWithPath("[].orderItems.[].orderQuantity").description("주문 개수").attributes(key("constraint").value("가게이름을 입력해주세요."))
                        )
                ));
    }

    @Test
    @Disabled
    void updateOrder() {
    }

    @Test
    void cancel() throws Exception {
        //expected
        mockMvc.perform(patch("/order/cancel/{orderId}", 1))
                .andExpect(status().isOk())
                .andDo(document("order/cancel"));
    }
}