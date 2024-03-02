package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.board.request.BoardCreate;
import com.yeoboya.lunch.api.v1.board.request.BoardSearch;
import com.yeoboya.lunch.api.v1.board.request.FileBoardCreate;
import com.yeoboya.lunch.config.SecretsManagerInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "lunch.yeoboya.com", uriPort = 4443)
@ExtendWith(RestDocumentationExtension.class)
@WithMockUser(username = "kimhyunjin@outlook.kr", roles = "USER")
@ContextConfiguration(initializers = SecretsManagerInitializer.class)
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
    @DisplayName("게시판 작성")
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
    @DisplayName("게시글 조회")
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
    @DisplayName("게시글 작성 (파일첨부)")
    void createPhotoTest() throws Exception {
        File fileResource = new ClassPathResource("test.jpg").getFile();
        byte[] fileBytes = Files.readAllBytes(fileResource.toPath());
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", fileBytes);

        FileBoardCreate fileBoardCreate = new FileBoardCreate("v@v.com", "파일업로드 테스트", Arrays.asList("#1", "#2", "#3"),
                "내용입니다.", 7777, false);

        String json = new ObjectMapper().writeValueAsString(fileBoardCreate);
        MockMultipartFile fileBoardCreateFile = new MockMultipartFile("fileBoardCreate", "",
                "application/json", json.getBytes());

        mockMvc.perform(multipart("/board/write/photo")
                        .file(file)
                        .file(fileBoardCreateFile)
                        .characterEncoding("utf-8")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("write/photo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("file").description("사진 파일"),
                                partWithName("fileBoardCreate").description("글 정보")
                        ),
                        requestFields(
                                fieldWithPath("email").description("Email").type(JsonFieldType.STRING),
                                fieldWithPath("title").description("게시글 제목").type(JsonFieldType.STRING),
                                fieldWithPath("content").description("게시글 내용").type(JsonFieldType.STRING),
                                fieldWithPath("hashTag").description("해시태그").type(JsonFieldType.ARRAY),
                                fieldWithPath("secret").description("게시글 공개여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("pin").description("게시글 비밀번호").type(JsonFieldType.NUMBER),
                                fieldWithPath("uploadType").description("파일 업로드 형태").optional().type(JsonFieldType.STRING),
                                fieldWithPath("file").description("업로드 파일 데이터").optional().type(JsonFieldType.STRING)
                        ),
                        responseFields(
                                fieldWithPath("code").description("The response code").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("The response message").type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    @DisplayName("게시글 단건 조회")
    public void testFindBoardById() throws Exception {
        Long boardId = 18L;
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
                                subsectionWithPath("data.hashTags").description("The list of hash tags associated with the board post")
                                        .type(JsonFieldType.ARRAY)
                                        .optional(),
                                fieldWithPath("data.hashTags[].tag").description("The specific tag of hash tag").optional(),
                                subsectionWithPath("data.files").description("The list of files associated with the board post")
                                        .type(JsonFieldType.ARRAY)
                                        .optional(),
                                fieldWithPath("data.files[].originalFileName").description("The original name of the uploaded file").optional(),
                                fieldWithPath("data.files[].fileName").description("The server generated file name for uploaded file").optional(),
                                fieldWithPath("data.files[].filePath").description("The file path where the file was saved").optional(),
                                fieldWithPath("data.files[].extension").description("The extension of the file").optional(),
                                fieldWithPath("data.files[].size").description("The size of the file in bytes").optional()
                        )));
    }
}
