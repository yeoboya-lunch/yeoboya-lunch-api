package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.Item.request.ItemCreate;
import com.yeoboya.lunch.api.v1.Item.request.ItemEdit;
import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import com.yeoboya.lunch.api.v1.common.response.Code;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 4443)
@ExtendWith(RestDocumentationExtension.class)
@WithMockUser(username = "kimhyunjin@outlook.kr", roles = "USER")
@TestPropertySource(properties = "jasypt.encryptor.password=RV47mq6CwLrDEankn8j4")
class BoardControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    private static int unique;

    @BeforeAll
    static void setUp() {
        unique = new Random().nextInt(10000);
    }

    @Test
    @DisplayName("게시판 저장 테스트")
    void saveBoardTest() throws Exception {

        BoardCreate boardCreate = BoardCreate.builder()
                .email("khj6804@gmail.com")
                .title("Test board title No is " + unique)
                .content("Test content")
                .hashTag(Arrays.asList("snatch", "clean&jerk", "크로스핏"))
                .secret(false)
                .pin(1234)
                .build();

        String json = objectMapper.writeValueAsString(boardCreate);

        mockMvc.perform(post("/board/write")
                        .with(user("khj6804@gmail.com"))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("board/write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("Email").type(JsonFieldType.STRING),
                                fieldWithPath("title").description("게시글 제목").type(JsonFieldType.STRING),
                                fieldWithPath("content").description("게시글 내용").type(JsonFieldType.STRING),
                                fieldWithPath("hashTag").description("해시태그").type(JsonFieldType.ARRAY),
                                fieldWithPath("secret").description("게시글 공개여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("pin").description("게시글 비밀번호").type(JsonFieldType.NUMBER)
                        ),
                        responseFields(
                                fieldWithPath("code").description("code").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message").type(JsonFieldType.STRING)
                        )
                ));
    }


    @Test
    @DisplayName("Board listing test")
    void listBoardTest() throws Exception {

        BoardSearch boardSearch = new BoardSearch(); // Fill this object with some test data

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("page", "0");
        info.add("size", "10");

        mockMvc.perform(get("/board")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE)
                        .params(info))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("board",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지").optional(),
                                parameterWithName("size").description("사이즈").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("The response code").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("The response message").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].boardId").description("게시판 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.list[].title").description("게시판 제목").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].content").description("게시판 내용").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].secret").description("게시판 공개 여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.list[].email").description("게시판 작성자 이메일").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].name").description("게시판 작성자 이름").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].createDate").description("작성일").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].hashTags[].tag").description("해시태그").type(JsonFieldType.STRING),
                                fieldWithPath("data.list[].files").description("첨부 파일").type(JsonFieldType.ARRAY),
                                fieldWithPath("data.pagination.isLast").description("마지막 페이지 여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.totalPages").description("전체 페이지 수").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.isFirst").description("첫 페이지 여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("data.pagination.page").description("현재 페이지 번호").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.totalElements").description("전체 게시물 수").type(JsonFieldType.NUMBER),
                                fieldWithPath("data.pagination.isEmpty").description("게시글 존재 여부").type(JsonFieldType.BOOLEAN)
                        )
                ));
    }

    @Test
    public void testFindBoardById() throws Exception {
        Long boardId = 1L; // change this value to an ID present in your database
        mockMvc.perform(get("/board/{id}", boardId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("find-board-by-id",
                        pathParameters(
                                parameterWithName("id").description("The id of the board to retrieve")
                        ),
                        responseFields(
                                fieldWithPath("code").description("The response code representing the result of the request"),
                                fieldWithPath("message").description("The message indicating the result of the request"),
                                fieldWithPath("data.boardId").description("The identifier of the board"),
                                fieldWithPath("data.title").description("The title of the board"),
                                fieldWithPath("data.content").description("The content of the board post"),
                                fieldWithPath("data.secret").description("The secret status of the board post"),
                                fieldWithPath("data.email").description("The email of the board post author"),
                                fieldWithPath("data.name").description("The name of the board post author"),
                                fieldWithPath("data.createDate").description("The creation date of the board post"),
                                fieldWithPath("data.hashTags[]").description("The list of hash tags associated with the board post"),
                                fieldWithPath("data.hashTags[].tag").description("The specific tag of hash tag"),
                                fieldWithPath("data.files[]").description("The list of files associated with the board post")
                        )));
    }
}
