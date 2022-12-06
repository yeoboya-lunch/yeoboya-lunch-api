package com.yeoboya.lunch.api.testContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.shop.request.ShopCreate;
import com.yeoboya.lunch.config.security.WithMockCustomUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@Slf4j
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@WithMockCustomUser
@ActiveProfiles("test")
class ContainerTest {

    @Autowired
    private MockMvc mockMvc;


    @Container
    private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.6.10")
            .withInitScript("data.sql");


    @Test
    void connect() {
        log.error("host: {}", mariaDBContainer.getHost());
        log.error("port: {}", mariaDBContainer.getMappedPort(3306));
        log.error("username: {}", mariaDBContainer.getUsername());
        log.error("password: {}", mariaDBContainer.getPassword());
        log.error("jdbc url: {}", mariaDBContainer.getJdbcUrl());
        try (Connection conn = DriverManager.getConnection(
                mariaDBContainer.getJdbcUrl(),
                mariaDBContainer.getUsername(),
                mariaDBContainer.getPassword())
        ) {
            log.error("got connection");
            // 코드
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void test() {
        assertThat(mariaDBContainer.isRunning()).isTrue();
    }


    @Test
    @DisplayName("상점등록")
    void create() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        //given
        ShopCreate request = new ShopCreate();
        request.setShopName("버거킹");
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


}