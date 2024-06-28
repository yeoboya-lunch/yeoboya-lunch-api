package com.yeoboya.lunch.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.domain.MemberInfo;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.domain.Role;
import com.yeoboya.lunch.config.security.domain.UserSecurityStatus;
import com.yeoboya.lunch.config.security.repository.RoleRepository;
import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.data.util.Pair;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestUtil {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;


    public TestUtil(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }


    public Pair<String, String> performLoginAndGetTokens(String loginId, String password) throws Exception {
        UserRequest.SignIn signIn = new UserRequest.SignIn();
        signIn.setLoginId(loginId);
        signIn.setPassword(password);

        String signInJson = this.objectMapper.writeValueAsString(signIn);

        MvcResult result = this.mockMvc.perform(post("/user/sign-in")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(signInJson))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        JsonNode root = this.objectMapper.readTree(contentAsString);
        String accessToken = root.path("data").path("accessToken").asText();
        String refreshToken = root.path("data").path("refreshToken").asText();

        return Pair.of(accessToken, refreshToken);
    }

    public RequestPostProcessor getToken(String loginId, String password){
        var ref = new Object() {
            Pair<String, String> tokens = null;
        };
        try {
            ref.tokens = this.performLoginAndGetTokens(loginId, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return mockHttpServletRequest -> {
            mockHttpServletRequest.addHeader("Authorization", "Bearer " + ref.tokens.getFirst());
            return mockHttpServletRequest;
        };
    }


}
