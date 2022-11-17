package com.yeoboya.lunch.api.v1.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.order.request.OrderCreate;
import com.yeoboya.lunch.api.v1.order.request.OrderItemCreate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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

        //given
        OrderCreate order = OrderCreate.builder()
                .email("khjzzm@gmail.com")
                .shopName("맥도날드")
                .orderItems(Collections.singletonList(orderItemCreate))
                .build();
        String json = objectMapper.writeValueAsString(order);

        //expected
        mockMvc.perform(post("/order")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("shopName").description("맥도날드"),
                                fieldWithPath("orderItems").description("주문아이템")
                        )
                ));
    }

    @Test
    void get() {
    }

    @Test
    void getList() {
    }

    @Test
    void edit() {
    }

    @Test
    void delete() {
    }
}