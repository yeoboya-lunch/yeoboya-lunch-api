package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.config.SecretsManagerInitializer;
import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.reqeust.AuthorityRequest;
import com.yeoboya.lunch.config.security.reqeust.SecurityRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
class RoleControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    @DisplayName("회원 권한리스트")
    void getAuthorityList() throws Exception {
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("page", "1");
        info.add("size", "10");

        mockMvc.perform(get("/role/authorities")
                        .params(info))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("role/authorities",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("Page number"),
                                parameterWithName("size").description("Page size")
                        ),
                        responseFields(
                                fieldWithPath("code").description("The response code"),
                                fieldWithPath("message").description("The response message"),
                                fieldWithPath("data").description("The main data object"),
                                fieldWithPath("data.pagination").description("Page information"),
                                fieldWithPath("data.pagination.page").description("Page number"),
                                fieldWithPath("data.pagination.totalPages").description("Total page count"),
                                fieldWithPath("data.pagination.totalElements").description("Total element count"),
                                fieldWithPath("data.pagination.first").description("Whether the page is the first page"),
                                fieldWithPath("data.pagination.last").description("Whether the page is the last page"),
                                fieldWithPath("data.pagination.empty").description("Whether the page contains any data"),
                                fieldWithPath("data.list[]").description("List of member roles"),
                                fieldWithPath("data.list[].email").description("Member's Email"),
                                fieldWithPath("data.list[].name").description("Member's Name"),
                                fieldWithPath("data.list[].roleDesc").description("Description of the Role"),
                                fieldWithPath("data.list[].enabled").description("Whether the member is enabled"),
                                fieldWithPath("data.list[].accountNonLocked").description("Whether the account is not locked")
                        )
                ));
    }

    @Test
    @DisplayName("회원 권한 업데이트")
    void updateAuthority() throws Exception {
        AuthorityRequest authorityRequest = new AuthorityRequest();
        authorityRequest.setEmail("role@test.com");
        authorityRequest.setRole(Authority.ROLE_MANAGER);

        mockMvc.perform(post("/role/authority-update")
                        .content(objectMapper.writeValueAsString(authorityRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("role/authority-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("Email of the member whose role is to be updated"),
                                fieldWithPath("role").description("New role for the member")
                        )
                ));
    }


    @Test
    @DisplayName("회원 계정 업데이트")
    void updateSecurity() throws Exception {
        SecurityRequest securityRequest = new SecurityRequest();
        securityRequest.setEmail("role@test.com");
        securityRequest.setEnabled(false);
        securityRequest.setAccountNonLocked(false);

        mockMvc.perform(post("/role/security-update")
                        .content(objectMapper.writeValueAsString(securityRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("role/security-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("Email of the member whose role is to be updated"),
                                fieldWithPath("enabled").description("Whether the member is enabled"),
                                fieldWithPath("accountNonLocked").description("Whether the account is not locked")
                        )
                ));
    }

}

