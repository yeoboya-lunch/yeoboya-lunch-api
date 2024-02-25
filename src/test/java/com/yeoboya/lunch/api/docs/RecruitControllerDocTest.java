package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.request.*;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 4443)
@ExtendWith(RestDocumentationExtension.class)
@WithMockUser(username = "kimhyunjin@outlook.kr", roles = "USER")
@TestPropertySource(properties = "jasypt.encryptor.password=RV47mq6CwLrDEankn8j4")
class RecruitControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;


    @Test
    @DisplayName("점심 주문 모집")
    void orderRecruit() throws Exception {
        // 현재 시간에 30분을 추가
        LocalDateTime localDateTimePlus30Mins = LocalDateTime.now().plusMinutes(30);
        Timestamp lastOrderTime = Timestamp.valueOf(localDateTimePlus30Mins);

        //given
        OrderRecruitmentCreate order = OrderRecruitmentCreate.builder()
                .email("khj@gmail.com")
                .title("맥도날드 먹는날")
                .lastOrderTime(lastOrderTime)
                .memo("신상버거 나왔습니다 ㅋㅋ")
                .deliveryFee(2500)
                .shopName("맥도날드")
                .build();

        String json = objectMapper.writeValueAsString(order);

        //expected
        mockMvc.perform(post("/order/recruit")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일").type(JsonFieldType.STRING),
                                fieldWithPath("title").description("모집 제목").type(JsonFieldType.STRING),
                                fieldWithPath("lastOrderTime").description("주문 마감 시간").type(JsonFieldType.STRING),
                                fieldWithPath("memo").description("메모").type(JsonFieldType.STRING),
                                fieldWithPath("deliveryFee").description("배달료").type(JsonFieldType.NUMBER),
                                fieldWithPath("shopName").description("가게 이름").type(JsonFieldType.STRING)
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
    @DisplayName("점심 주문 참가")
    @Transactional
    void testJoinGroupOrder() throws Exception {
        // given
        List<OrderItemCreate> orderItemsList = new ArrayList<>();

        OrderItemCreate testItem = OrderItemCreate.builder()
                .itemName("슈슈버거")
                .orderQuantity(1)
                .build();
        orderItemsList.add(testItem);

        GroupOrderJoin groupOrderJoin = GroupOrderJoin.builder()
                .orderNo(5L)
                .email("khj3103@gmail.com")
                .orderItems(orderItemsList)
                .build();

        String json = objectMapper.writeValueAsString(groupOrderJoin);

        // when & then
        mockMvc.perform(post("/order/recruit/group/join")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruit/group/join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderNo").description("Order number").type(JsonFieldType.NUMBER),
                                fieldWithPath("email").description("Email of the order").type(JsonFieldType.STRING),
                                fieldWithPath("orderItems[].itemName").description("Name of the order item").type(JsonFieldType.STRING),
                                fieldWithPath("orderItems[].orderQuantity").description("Quantity of the order item").type(JsonFieldType.NUMBER)
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
    @DisplayName("점심 주문 조회")
    void testFetchLunchRecruits() throws Exception {
        OrderSearch search = new OrderSearch();
        search.setOrderStatus(OrderStatus.valueOf("ORDER_START"));
        search.setOrderName("김현진");
        search.setOrderEmail("");
        search.setStartDate(LocalDate.ofEpochDay(20240101));
        search.setEndDate(LocalDate.ofEpochDay(20241231));

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("orderStatus", search.getOrderStatus().name());
        info.add("orderName", search.getOrderName());
        info.add("orderEmail", search.getOrderEmail());
        info.add("startDate", "20240101");
        info.add("endDate", "20241231");
        info.add("page", "0");
        info.add("size", "10");

        // when & then
        mockMvc.perform(get("/order/recruits")
                        .params(info))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruits",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("orderStatus").description("Order Status"),
                                parameterWithName("orderName").description("Order Name"),
                                parameterWithName("orderEmail").description("Order Email"),
                                parameterWithName("startDate").description("Start Date"),
                                parameterWithName("endDate").description("End Date"),
                                parameterWithName("page").description("페이지").optional(),
                                parameterWithName("size").description("사이즈").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("The response code").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("The response message").type(JsonFieldType.STRING),
                                fieldWithPath("data").description("The main data object").type(JsonFieldType.OBJECT),
                                fieldWithPath("data.hasNext").description("Whether there are more pages").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.isFirst").description("Whether request page is the first").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.hasPrevious").description("Whether there are previous pages").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.isLast").description("Whether request page is the last").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.list[]").description("List of orders").type(JsonFieldType.ARRAY),
                                fieldWithPath("data.list[].orderId").description("Order ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.list[].orderMemberEmail").description("Order member Email").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].orderMemberName").description("Order member Name").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].shopName").description("Shop Name").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].title").description("Order title").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].lastOrderTime").description("Last order time").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].orderStatus").description("Order status").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].groupCount").description("Group count").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pageNo").description("Page number").type(JsonFieldType.NUMBER)
                        )
                ));
    }


    @Test
    @DisplayName("Lunch Recruit Status Update")
    void updateLunchRecruitStatus() throws Exception {
        //given
        String orderId = "20";  //Provide the orderId
        OrderEdit orderEdit = new OrderEdit();
        orderEdit.setStatus(OrderStatus.END.name());

        String jsonRequest = objectMapper.writeValueAsString(orderEdit);

        //expected
        mockMvc.perform(patch("/order/recruit/{orderId}", orderId)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruit-status",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("status").description("Order Status").type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("code").description("response code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("response message")
                                        .type(JsonFieldType.STRING)
                        )
                ));
    }
    @Test
    @DisplayName("Order History Get")
    void getOrderHistoryByEmail() throws Exception {
        //given
        String email = "3@3.com";  //Provide the email

        //expected
        mockMvc.perform(get("/order/recruit/history/{email}", email)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order/recruit/history",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("response code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("response message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.isLast").description("is it the last page")
                                        .type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.hasPrevious").description("does it have previous page")
                                        .type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.isFirst").description("is it the first page")
                                        .type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.hasNext").description("does it have next page")
                                        .type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pageNo").description("page number")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.list[0].groupOrderId").description("ID of group order")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.list[0].email").description("email of the user")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.list[0].name").description("name of the user")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.list[0].orderItem[0].itemName").description("name of the item")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.list[0].orderItem[0].orderPrice").description("price per item ordered")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.list[0].orderItem[0].orderQuantity").description("quantity of item ordered")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.list[0].orderItem[0].totalPrice").description("total price for this item")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.list[0].totalPrice").description("total price of the order")
                                        .type(JsonFieldType.NUMBER)
                        )
                ));
    }
}
