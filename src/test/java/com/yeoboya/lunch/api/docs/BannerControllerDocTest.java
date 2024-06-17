package com.yeoboya.lunch.api.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v1.event.domain.Banner;
import com.yeoboya.lunch.api.v1.event.repository.BannerRepository;
import com.yeoboya.lunch.api.v1.event.reqeust.BannerRequest;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;

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
@ContextConfiguration
class BannerControllerDocTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private BannerRepository bannerRepository;

    @Test
    @DisplayName("배너 생성")
    void createBanner() throws Exception {
        BannerRequest bannerRequest = new BannerRequest();
        bannerRequest.setTitle("Spring Sale");
        bannerRequest.setDisplayOrder(1);
        bannerRequest.setStartDate(LocalDateTime.now().minusDays(1));
        bannerRequest.setEndDate(LocalDateTime.now().plusDays(30));
        bannerRequest.setDisplayLocation(Banner.DisplayLocation.MAIN_PAGE);

        File fileResource = new ClassPathResource("images/test.jpg").getFile();
        byte[] fileBytes = Files.readAllBytes(fileResource.toPath());
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", fileBytes);


        // Convert BannerRequest object to String
        String jsonBannerRequest = objectMapper.writeValueAsString(bannerRequest);
        MockMultipartFile bannerRequestPart = new MockMultipartFile(
                "bannerRequest",
                "",
                "application/json",
                jsonBannerRequest.getBytes());

        mockMvc.perform(multipart("/banners")
                        .file(file)
                        .file(bannerRequestPart)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("banner/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("file").description("배너 이미지 파일"),
                                partWithName("bannerRequest").description("배너 정보")
                        ),
                        requestPartFields("bannerRequest",
                                fieldWithPath("title").description("배너 제목").type(JsonFieldType.STRING),
                                fieldWithPath("displayOrder").description("배너 이미지 순서").type(JsonFieldType.NUMBER),
                                fieldWithPath("startDate").description("배너 표시 시작일자").type(JsonFieldType.STRING),
                                fieldWithPath("endDate").description("배너 표시 종료일자").type(JsonFieldType.STRING),
                                fieldWithPath("displayLocation").description("배너 표시 위치 (MAIN_PAGE, MY_PAGE)").type(JsonFieldType.STRING)
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
    @DisplayName("배너 목록 조회")
    void getBanners() throws Exception {
        mockMvc.perform(get("/banners")
                        .param("displayLocation", "MAIN_PAGE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("banner/get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("displayLocation").description("배너 표시 위치 (MAIN_PAGE, MY_PAGE)").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("code").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("message").type(JsonFieldType.STRING),
                                fieldWithPath("data[].id").description("배너 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].title").description("배너 제목").type(JsonFieldType.STRING),
                                fieldWithPath("data[].displayOrder").description("배너 이미지 순서").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].startDate").description("배너 표시 시작일자").type(JsonFieldType.STRING),
                                fieldWithPath("data[].endDate").description("배너 표시 종료일자").type(JsonFieldType.STRING),
                                fieldWithPath("data[].displayLocation").description("배너 표시 위치").type(JsonFieldType.STRING),
                                fieldWithPath("data[].bannerFiles[]").description("배너 파일").type(JsonFieldType.ARRAY),
                                fieldWithPath("data[].bannerFiles[].id").description("배너 파일 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].bannerFiles[].originalFileName").description("배너 원본 파일 이름").type(JsonFieldType.STRING),
                                fieldWithPath("data[].bannerFiles[].fileName").description("배너 파일 이름").type(JsonFieldType.STRING),
                                fieldWithPath("data[].bannerFiles[].filePath").description("배너 파일경로").type(JsonFieldType.STRING),
                                fieldWithPath("data[].bannerFiles[].extension").description("배너 파일 확장자").type(JsonFieldType.STRING),
                                fieldWithPath("data[].bannerFiles[].size").description("배너 파일 크기").type(JsonFieldType.NUMBER),
                                fieldWithPath("data[].bannerFiles[].url").description("배너 이미지 URL").type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    @DisplayName("배너 삭제")
    void deleteBanner() throws Exception {
        // given
        Banner banner = Banner.builder()
                .title("Spring Sale")
                .displayOrder(1)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(30))
                .displayLocation(Banner.DisplayLocation.MAIN_PAGE)
                .build();
        banner = bannerRepository.save(banner);

        // when & then
        mockMvc.perform(delete("/banners/{id}", banner.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("banner/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").description("응답 코드").type(JsonFieldType.NUMBER),
                                fieldWithPath("message").description("응답 메시지").type(JsonFieldType.STRING)
                        )
                ));
    }
}
