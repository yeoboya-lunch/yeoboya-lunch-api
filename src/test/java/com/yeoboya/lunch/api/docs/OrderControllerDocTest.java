package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.order.constants.OrderStatus;
import com.yeoboya.lunch.api.v1.order.request.*;
import com.yeoboya.lunch.api.v1.order.service.OrderService;
import com.yeoboya.lunch.api.v1.shop.service.ShopService;
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
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
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

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShopService shopService;

    private TestUtil testUtil;

    @BeforeEach
    void setUp() {
        testUtil = new TestUtil(mockMvc, objectMapper);
    }

    @Test
    @DisplayName("점심 주문 모집 시작")
    void testStartLunchOrderRecruitment() throws Exception {
        // 현재 시간에 30분을 추가
        LocalDateTime localDateTimePlus30Mins = LocalDateTime.now().plusMinutes(30);
        Timestamp lastOrderTime = Timestamp.valueOf(localDateTimePlus30Mins);

        //given
        OrderRecruitmentCreate order = OrderRecruitmentCreate.builder()
                .loginId("admin")
                .title("1867 드실분")
                .lastOrderTime(lastOrderTime)
                .memo("신상메뉴 나왔습니다!!")
                .deliveryFee(1000)
                .shopName(shopService.selectShop().getName())
                .build();

        String json = objectMapper.writeValueAsString(order);

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        //expected
        mockMvc.perform(post("/order/recruit/start")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
                        .with(postProcessor)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruit/start",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").description("로그인 아이디").type(JsonFieldType.STRING),
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
        // 현재 시간에 30분을 추가
        LocalDateTime localDateTimePlus30Mins = LocalDateTime.now().plusMinutes(30);
        Timestamp lastOrderTime = Timestamp.valueOf(localDateTimePlus30Mins);

        orderService.startLunchOrderRecruitment(OrderRecruitmentCreate.builder()
                .loginId("admin")
                .title("1867 드실분")
                .lastOrderTime(lastOrderTime)
                .memo("신상메뉴 나왔습니다!!")
                .deliveryFee(1000)
                .shopName(shopService.selectShop().getName())
                .build()
        );

        OrderSearch search = new OrderSearch();
        search.setOrderStatus(OrderStatus.valueOf("ORDER_START"));
        search.setOrderName("");
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
                                fieldWithPath("data.list[].orderMemberLoginId").description("Order member id").type(JsonFieldType.STRING),
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
        // 현재 시간에 30분을 추가
        LocalDateTime localDateTimePlus30Mins = LocalDateTime.now().plusMinutes(30);
        Timestamp lastOrderTime = Timestamp.valueOf(localDateTimePlus30Mins);

        orderService.startLunchOrderRecruitment(OrderRecruitmentCreate.builder()
                .loginId("admin")
                .title("1867 드실분")
                .lastOrderTime(lastOrderTime)
                .memo("신상메뉴 나왔습니다!!")
                .deliveryFee(1000)
                .shopName(shopService.selectShop().getName())
                .build()
        );

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        // when & then
        mockMvc.perform(get("/order/recruit/{orderId}", 35)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .with(postProcessor)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruit/orderId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("주문번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Response Code").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("Response Message").type(JsonFieldType.STRING),

                                // 상점 데이터
                                fieldWithPath("data.shop.shopName").description("상점 이름").type(JsonFieldType.STRING),
                                fieldWithPath("data.shop.items[].name").description("상품명").type(JsonFieldType.STRING),
                                fieldWithPath("data.shop.items[].price").description("상품 가격").type(JsonFieldType.NUMBER),

                                // 주문 멤버 데이터
                                fieldWithPath("data.orderMember.loginId").description("Order member id").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.orderMember.email").description("Order member Email").type(JsonFieldType.STRING),
                                fieldWithPath("data.orderMember.name").description("Order member Name").type(JsonFieldType.STRING),
                                fieldWithPath("data.orderMember.provider").description("Order member provider").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.orderMember.bankName").description("Order member bank name").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.orderMember.accountNumber").description("Order member account number").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.orderMember.bio").description("Order member bio").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.orderMember.nickName").description("Order member nickname").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.orderMember.phoneNumber").description("Order member phone number").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("data.orderMember.isPrimaryProfileImg").description("State if primary profile image").type(JsonFieldType.BOOLEAN).optional(),
                                fieldWithPath("data.orderMember.fileUploadResponses").description("File upload response").type(JsonFieldType.ARRAY).optional(),
                                fieldWithPath("data.orderMember.account").description("State if account").type(JsonFieldType.BOOLEAN).optional(),
                                fieldWithPath("data.orderMember.profileImg").optional().description("Order member profile image").type(JsonFieldType.STRING),

                                // 그룹 데이터
                                fieldWithPath("data.group[].orderId").description("Order ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.group[].groupOrderId").description("Group Order ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.group[].title").description("Order Title").type(JsonFieldType.STRING),
                                fieldWithPath("data.group[].email").description("Order member Email").type(JsonFieldType.STRING),
                                fieldWithPath("data.group[].name").description("Order member Name").type(JsonFieldType.STRING),
                                fieldWithPath("data.group[].orderItem[].itemName").description("Order item name").type(JsonFieldType.STRING),
                                fieldWithPath("data.group[].orderItem[].orderPrice").description("Order item price").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.group[].orderItem[].orderQuantity").description("Order item quantity").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.group[].orderItem[].totalPrice").description("Total price for order item").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.group[].totalPrice").description("Total price for a group").type(JsonFieldType.NUMBER),

                                // 주문 데이터
                                fieldWithPath("data.order.orderId").description("Order ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.order.title").description("Order Title").type(JsonFieldType.STRING),
                                fieldWithPath("data.order.lastOrderTime").description("Order last time").type(JsonFieldType.STRING),
                                fieldWithPath("data.order.orderStatus").description("Order Status").type(JsonFieldType.STRING),
                                fieldWithPath("data.order.memo").description("Order Memo").type(JsonFieldType.STRING),
                                fieldWithPath("data.order.deliveryFee").description("Delivery Fee").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.order.joinMember").description("Joining member to an order").type(JsonFieldType.ARRAY),


                                // 누락된 order.joinMember 필드
                                fieldWithPath("data.order.joinMember[].orderId").description("Order id").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.order.joinMember[].groupOrderId").description("Group Order id").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.order.joinMember[].title").description("Order title").type(JsonFieldType.STRING),
                                fieldWithPath("data.order.joinMember[].email").description("Order member Email").type(JsonFieldType.STRING),
                                fieldWithPath("data.order.joinMember[].name").description("Order member Name").type(JsonFieldType.STRING),
                                fieldWithPath("data.order.joinMember[].orderItem[].itemName").description("Order item name").type(JsonFieldType.STRING),
                                fieldWithPath("data.order.joinMember[].orderItem[].orderPrice").description("Order item price").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.order.joinMember[].orderItem[].orderQuantity").description("Order item quantity").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.order.joinMember[].orderItem[].totalPrice").description("Total order item price").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.order.joinMember[].totalPrice").description("Total order price").type(JsonFieldType.NUMBER)
                        )
                ));
    }

    @Test
    @DisplayName("점심 주문 모집에 참여")
    @Transactional
    void joinGroupOrder() throws Exception {
        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        // given
        List<OrderItemCreate> orderItemsList = new ArrayList<>();
        orderItemsList.add(
                OrderItemCreate.builder()
                        .itemName("Banana")
                        .orderQuantity(1)
                        .build()
        );

        GroupOrderJoin groupOrderJoin = GroupOrderJoin.builder()
                .orderId(3L)
                .loginId("admin")
                .orderItems(orderItemsList)
                .build();

        String json = objectMapper.writeValueAsString(groupOrderJoin);

        // when & then
        mockMvc.perform(post("/order/recruit/join")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
                        .with(postProcessor)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("order/recruit/join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("orderId").description("Order number").type(JsonFieldType.NUMBER),
                                fieldWithPath("loginId").description("loginId of the order").type(JsonFieldType.STRING),
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
    @DisplayName("점심 주문 수정")
    @Transactional
    void testJoinUpdateItem() throws Exception {

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        // 주문 수정 생성
        List<OrderItemCreateEdit> orderItemCreateEdits = new ArrayList<>();
        orderItemCreateEdits.add(
                OrderItemCreateEdit.builder()
                    .itemName("Banana")
                    .orderQuantity(10)
                    .build()
        );

        // 주문 수정 데이터 생성
        GroupOrderJoinEdit groupOrderJoinEdit = new GroupOrderJoinEdit();
        groupOrderJoinEdit.setOrderId(1L);
        groupOrderJoinEdit.setGroupOrderId(1L);
        groupOrderJoinEdit.setOrderItems(orderItemCreateEdits);

        // 객체를 JSON으로 변환
        String json = objectMapper.writeValueAsString(groupOrderJoinEdit);

        // when & then
        mockMvc.perform(patch("/order/recruit/join")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
                        .with(postProcessor)
                )
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
    @DisplayName("내 주문 내역 조회")
    void testRetrieveMyOrderHistoryByEmail() throws Exception {
        //given
        String loginId = "order";

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        //expected
        mockMvc.perform(get("/order/recruit/histories/join/{loginId}", loginId)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .with(postProcessor)
                )
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
                                fieldWithPath("data.list[]").description("Order")
                                        .type(JsonFieldType.ARRAY).optional(),
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
    @DisplayName("내 주문 모집 내역 조회")
    void testRetrieveMyOrderRecruitmentHistoryByEmail() throws Exception {
        //given
        String loginId = "admin";

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        //expected
        mockMvc.perform(get("/order/recruit/histories/{loginId}", loginId)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .with(postProcessor)
                )
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
        String orderId = "1";  //Provide the orderId
        OrderEdit orderEdit = new OrderEdit();
        orderEdit.setStatus(OrderStatus.END.name());

        String jsonRequest = objectMapper.writeValueAsString(orderEdit);

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        //expected
        mockMvc.perform(patch("/order/recruit/{orderId}", orderId)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(jsonRequest)
                        .with(postProcessor)
                )
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

        RequestPostProcessor postProcessor = testUtil.getToken("admin", "qwer1234@@");

        // given
        Long groupOrderId = 2L;  // provide the groupOrderId you want to cancel

        // when & then
        mockMvc.perform(delete("/order/recruit/join/{groupOrderId}", groupOrderId)
                        .contentType(APPLICATION_JSON)
                        .with(postProcessor)
                )
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
