package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.config.SecretsManagerInitializer;
import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.yeoboya-lunch.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@ContextConfiguration(initializers = SecretsManagerInitializer.class)
class UserControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    private static String uniqueEmail;

    @BeforeAll
    static void setUp() {
        uniqueEmail = "test" + new Random().nextInt(10000) + "@example.com";
    }

    @Test
    @DisplayName("회원가입")
    void signUp() throws Exception {
        //given
        UserRequest.SignUp signUp = new UserRequest.SignUp();
        signUp.setEmail(uniqueEmail);
        signUp.setName("김현진");
        signUp.setPassword("1234qwer!@#$");

        String json = objectMapper.writeValueAsString(signUp);

        //expected
        mockMvc.perform(post("/user/sign-up")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("user/sign-up",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("password").description("비밀번호")
                                        .attributes(key("note").value("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요."))
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
    @DisplayName("로그인")
    void login() throws Exception {
        //given
        UserRequest.SignIn signIn = new UserRequest.SignIn();
        signIn.setEmail(uniqueEmail);
        signIn.setPassword("1234qwer!@#$");

        String json = objectMapper.writeValueAsString(signIn);

        //expected
        mockMvc.perform(post("/user/sign-in")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/sign-in",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("code")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.subject").description("이메일")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.id").description("고유번호")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.issuer").description("발행자")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.issueDAt").description("발행일자")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.accessToken").description("토큰")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.refreshToken").description("리프레시 토큰")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.tokenExpirationTime").description("토큰 만료기간")
                                        .type(JsonFieldType.STRING),
                                fieldWithPath("data.refreshTokenExpirationTime").description("리프레시 토큰 만료기간")
                                        .type(JsonFieldType.NUMBER),
                                fieldWithPath("data.refreshTokenExpirationTimeStr").description("리프레시 토큰 만료기간")
                                        .type(JsonFieldType.STRING)

                        )
                ));
    }

    private Pair<String, String> performLoginAndGetTokens() throws Exception {
        UserRequest.SignIn signIn = new UserRequest.SignIn();
        signIn.setEmail("logout@gmail.com");
        signIn.setPassword("qwer1234@@");

        String signInJson = objectMapper.writeValueAsString(signIn);

        MvcResult result = mockMvc.perform(post("/user/sign-in")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(signInJson))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(contentAsString);
        String accessToken = root.path("data").path("accessToken").asText();
        String refreshToken = root.path("data").path("refreshToken").asText();

        // accessToken과 refreshToken을 반환
        return Pair.of(accessToken, refreshToken);
    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {
        //given
        Pair<String, String> tokens = performLoginAndGetTokens();

        UserRequest.SignOut signOut = new UserRequest.SignOut();
        signOut.setAccessToken(tokens.getFirst());
        signOut.setRefreshToken(tokens.getSecond());

        String json = objectMapper.writeValueAsString(signOut);

        // When & then
        mockMvc.perform(post("/user/sign-out")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/sign-out",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("accessToken").description("액세스 토큰"),
                                fieldWithPath("refreshToken").description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("메세지").type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 변경")
    void changePassword() throws Exception {
        //given
        UserRequest.Credentials credentials = new UserRequest.Credentials();
        credentials.setEmail(uniqueEmail);
        credentials.setOldPassword("1234qwer!@#$");
        credentials.setNewPassword("qwer1234@@");
        credentials.setConfirmNewPassword("qwer1234@@");

        String json = objectMapper.writeValueAsString(credentials);

        //expected
        mockMvc.perform(patch("/user/setting/security")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/setting/security",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("oldPassword").description("전 비밀번호"),
                                fieldWithPath("newPassword").description("새로운 비밀번호"),
                                fieldWithPath("confirmNewPassword").description("비밀번호 확인"),
                                fieldWithPath("passKey").description("KnowPassKey").ignored()
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
    @DisplayName("토큰 재발급")
    public void reIssueToken() throws Exception {
        //given
        Pair<String, String> tokens = performLoginAndGetTokens();

        UserRequest.Reissue reissue = new UserRequest.Reissue();
        reissue.setRefreshToken(tokens.getSecond());

        String json = objectMapper.writeValueAsString(reissue);

        ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(UserRequest.Reissue.class);

        //expected
        mockMvc.perform(post("/user/reissue")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/reissue",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("accessToken")
                                        .description("엑세스 토큰")
                                        .attributes(
                                                key("constraints")
                                                        .value(
                                                                StringUtils.collectionToDelimitedString(
                                                                        constraintDescriptions.descriptionsForProperty("accessToken"), ". "
                                                                )
                                                        )
                                        ),

                                fieldWithPath("refreshToken")
                                        .description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("메시지").type(JsonFieldType.STRING),
                                fieldWithPath("data.subject").description("서브젝트").type(JsonFieldType.STRING),
                                fieldWithPath("data.id").description("아이디").type(JsonFieldType.STRING),
                                fieldWithPath("data.issuer").description("발행자").type(JsonFieldType.STRING),
                                fieldWithPath("data.issueDAt").description("발행일시").type(JsonFieldType.STRING),
                                fieldWithPath("data.accessToken").description("액세스 토큰").type(JsonFieldType.STRING),
                                fieldWithPath("data.refreshToken").description("리프레시 토큰").type(JsonFieldType.STRING),
                                fieldWithPath("data.tokenExpirationTime").description("토큰 만료일시").type(JsonFieldType.STRING),
                                fieldWithPath("data.refreshTokenExpirationTime").description("리프레시토큰 만료일시").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.refreshTokenExpirationTimeStr").description("리프레시 토큰 만료일시 문자열").type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 초기화 링크")
    public void sendResetPasswordMail() throws Exception {
        //given
        UserRequest.ResetPassword resetPassword = new UserRequest.ResetPassword();
        resetPassword.setEmail("logout@gmail.com");
        resetPassword.setPhone("010-0000-0000");
        resetPassword.setAuthorityLink("localhost");

        String json = objectMapper.writeValueAsString(resetPassword);

        //expected
        mockMvc.perform(post("/user/sendResetPasswordMail")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/sendResetPasswordMail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("phone").description("휴대폰 번호"),
                                fieldWithPath("authorityLink").description("권한 링크")
                        ),
                        responseFields(
                                fieldWithPath("code").description("코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("메시지").type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 초기화")
    public void resetPassword() throws Exception {
        // Arrange
        UserRequest.Credentials credentials = new UserRequest.Credentials();
        credentials.setEmail("logout@gmail.com");
        credentials.setOldPassword("oldPassword123!");  // Assuming old password
        credentials.setNewPassword("newPassword123!");
        credentials.setConfirmNewPassword("newPassword123!");
        credentials.setPassKey("passKey123");  // Assuming you have a passKey

        String json = objectMapper.writeValueAsString(credentials);

        // Act
        ResultActions result = mockMvc.perform(
                patch("/user/resetPassword")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
        );

        // Assert
        result
                .andDo(print())
//                .andExpect(status().isOk())
                .andDo(document(
                        "user/resetPassword",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("oldPassword").description("이전 비밀번호"),
                                fieldWithPath("newPassword").description("새 비밀번호"),
                                fieldWithPath("confirmNewPassword").description("새 비밀번호 확인"),
                                fieldWithPath("passKey").description("비밀번호 재설정용 키")
                        ),
                        responseFields(
                                fieldWithPath("code").description("코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("메시지").type(JsonFieldType.STRING)
                        )
                ));
    }

}
