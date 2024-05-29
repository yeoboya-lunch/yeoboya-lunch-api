package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.request.*;
import com.yeoboya.lunch.config.SecretsManagerInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
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
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.yeoboya-lunch.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@ContextConfiguration(initializers = SecretsManagerInitializer.class)
class OrderControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;


    @Test
    @DisplayName("점심 주문 모집 시작")
    void testStartLunchOrderRecruitment() throws Exception {
        // 현재 시간에 30분을 추가
        LocalDateTime localDateTimePlus30Mins = LocalDateTime.now().plusMinutes(30);
        Timestamp lastOrderTime = Timestamp.valueOf(localDateTimePlus30Mins);

        //given
        OrderRecruitmentCreate order = OrderRecruitmentCreate.builder()
                .email("order@test.com")
                .title("맥도날드 드실분")
                .lastOrderTime(lastOrderTime)
                .memo("신상버거 나왔습니다!!")
                .deliveryFee(1000)
                .shopName("맥도날드")
                .build();

        String json = objectMapper.writeValueAsString(order);

        //expected
        mockMvc.perform(post("/order/recruit/start")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruit/start",
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
    @DisplayName("점심 주문 모집 리스트")
    void testListLunchOrderRecruitment() throws Exception {
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
                                fieldWithPath("data.pagination").description("Pagination information").type(JsonFieldType.OBJECT),
                                fieldWithPath("data.pagination.pageNo").description("Page number").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.size").description("Total number of elements on page").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.numberOfElements").description("Current number of elements").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.isFirst").description("Whether the page is the first page").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.isLast").description("Whether the page is the last page").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.hasNext").description("Whether there are more pages").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.hasPrevious").description("Whether there are previous pages").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.list[]").description("List of orders").type(JsonFieldType.ARRAY).optional(),
                                fieldWithPath("data.list[].orderId").description("Order ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.list[].orderMemberEmail").description("Order member Email").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].orderMemberName").description("Order member Name").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].shopName").description("Shop Name").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].title").description("Order title").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].lastOrderTime").description("Last order time").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].orderStatus").description("Order status").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].groupCount").description("Group count").type(JsonFieldType.NUMBER)
                        )
                ));
    }

    @Test
    @DisplayName("주문번호로 점심 주문 정보 조회")
    void testRetrieveLunchOrderInfoByOrderId() throws Exception {
        // when & then
        mockMvc.perform(get("/order/recruits/", 1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruits/orderId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지").optional(),
                                parameterWithName("size").description("사이즈").optional()
                        ),

                        responseFields(
                                fieldWithPath("code").description("Response Code").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("Response Message").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].orderId").description("Order ID").type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data.list[].orderMemberEmail").description("Order member Email").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].orderMemberName").description("Order member Name").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].shopName").description("Order Shop Name").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].title").description("Order Title").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].lastOrderTime").description("Order Last Time").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].orderStatus").description("Order status").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].groupCount").description("Group count").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.isLast").description("Whether request page is the last").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.hasPrevious").description("Whether there are previous pages").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.isFirst").description("Whether request page is the first").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.hasNext").description("Whether there are more pages").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.pageNo").description("The page number").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.size").description("The size of the page").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.numberOfElements").description("The number of elements on this page").type(JsonFieldType.NUMBER)
                        )
                ));
    }

    @Test
    @DisplayName("점심 주문 모집에 참여")
    @Transactional
    void testParticipationInLunchOrderRecruitment() throws Exception {
        // given
        List<OrderItemCreate> orderItemsList = new ArrayList<>();

        OrderItemCreate testItem = OrderItemCreate.builder()
                .itemName("아이템7711")
                .orderQuantity(1)
                .build();
        orderItemsList.add(testItem);

        GroupOrderJoin groupOrderJoin = GroupOrderJoin.builder()
                .orderId(1L)
                .email("join2@test.com")
                .orderItems(orderItemsList)
                .build();

        String json = objectMapper.writeValueAsString(groupOrderJoin);

        // when & then
        mockMvc.perform(post("/order/recruit/join")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruit/join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderId").description("Order number").type(JsonFieldType.NUMBER),
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
    @DisplayName("점심 주문 모집에 참여 후 주문 수정")
    @Transactional
    void testEditParticipationInLunchOrderRecruitment() throws Exception {
        // 주문 항목 생성
        List<OrderItemCreateEdit> orderItemsList = new ArrayList<>();

        OrderItemCreateEdit testItem = OrderItemCreateEdit.builder()
                .itemName("우럭")
                .orderQuantity(3)
                .build();

        orderItemsList.add(testItem);

        // 주문 수정 데이터 생성
        GroupOrderJoinEdit groupOrderJoinEdit = new GroupOrderJoinEdit();
        groupOrderJoinEdit.setOrderId(64L);
        groupOrderJoinEdit.setGroupOrderId(74L);
        groupOrderJoinEdit.setOrderItems(orderItemsList);

        // 객체를 JSON으로 변환
        String json = objectMapper.writeValueAsString(groupOrderJoinEdit);

        // when & then
        mockMvc.perform(patch("/order/recruit/join")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruit/join/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderId").description("주문 번호").type(JsonFieldType.NUMBER),
                                fieldWithPath("groupOrderId").description("모임 주문 번호").type(JsonFieldType.NUMBER),
                                fieldWithPath("orderItems[].itemName").description("주문 상품 이름").type(JsonFieldType.STRING),
                                fieldWithPath("orderItems[].orderQuantity").description("주문 상품 수량").type(JsonFieldType.NUMBER)
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("응답 메세지").type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    @DisplayName("내 주문 내역 조회 (이메일)")
    void testRetrieveMyOrderHistoryByEmail() throws Exception {
        //given
        String email = "join@test.com";

        //expected
        mockMvc.perform(get("/order/recruit/histories/join/{email}", email)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order/recruit/histories/join/email",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("response code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("response message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.pagination.isLast").description("is it the last page")
                                        .type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.hasPrevious").description("does it have previous page")
                                        .type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.isFirst").description("is it the first page")
                                        .type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.hasNext").description("does it have next page")
                                        .type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.pageNo").description("page number")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.size").description("Total number of elements on page")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.numberOfElements").description("Current number of elements")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.list[].orderId").description("Order ID")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data.list[].groupOrderId").description("ID of group order")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data.list[].title").description("Title of the order")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.list[].email").description("email of the user")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.list[].name").description("name of the user")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.list[].orderItem[].itemName").description("name of the item")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.list[].orderItem[].orderPrice").description("price per item ordered")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data.list[].orderItem[].orderQuantity").description("quantity of item ordered")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data.list[].orderItem[].totalPrice").description("total price for this item")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data.list[].totalPrice").description("total price of the order")
                                        .type(JsonFieldType.NUMBER).optional()
                        )
                ));
    }

    @Test
    @DisplayName("내 주문 모집 내역 조회 (이메일)")
    void testRetrieveMyOrderRecruitmentHistoryByEmail() throws Exception {
        //given
        String email = "order@test.com";

        //expected
        mockMvc.perform(get("/order/recruit/histories/{email}", email)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order/recruit/histories/email",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("response code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("response message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data[].orderId").description("Order ID")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].title").description("Title of the order")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data[].lastOrderTime").description("Last order time")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data[].orderStatus").description("Status of the order")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data[].memo").description("Order memo")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data[].deliveryFee").description("Delivery fee of the order")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].joinMember[]").description("Joined members' order info")
                                        .type(JsonFieldType.ARRAY).optional(),
                                fieldWithPath("data[].joinMember[].orderId").description("Joined member's order ID")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data[].joinMember[].groupOrderId").description("ID of group order")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data[].joinMember[].title").description("Title of join member's order")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].joinMember[].email").description("Join member's email")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].joinMember[].name").description("Join member's name")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].joinMember[].orderItem[].itemName").description("Name of item in join member's order")
                                        .type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data[].joinMember[].orderItem[].orderPrice").description("Price per item in join member's order")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data[].joinMember[].orderItem[].orderQuantity").description("Quantity of item in join member's order")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data[].joinMember[].orderItem[].totalPrice").description("Total price of join member's order item")
                                        .type(JsonFieldType.NUMBER).optional(),
                                fieldWithPath("data[].joinMember[].totalPrice").description("Total price of join member's order")
                                        .type(JsonFieldType.NUMBER)
                        )
                ));
    }


    @Test
    @DisplayName("주문모집 상태변경")
    @Transactional
    void changeRecruitmentOrderStatus() throws Exception {
        //given
        String orderId = "2";  //Provide the orderId
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
                .andDo(document("order/recruit/orderId",
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
    @DisplayName("점심 주문 취소 (주문나가기)")
    @Transactional
    void cancelLunchOrder() throws Exception {
        // given
        Long groupOrderId = 4L;  // provide the groupOrderId you want to cancel

        // when & then
        mockMvc.perform(delete("/order/recruit/join/{groupOrderId}", groupOrderId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("order/recruit/join/cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("Response Code").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("Response Message").type(JsonFieldType.STRING)
                        )
                ));

    }
}
