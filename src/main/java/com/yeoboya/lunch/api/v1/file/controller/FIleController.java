package com.yeoboya.lunch.api.v1.file.controller;

import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import com.yeoboya.lunch.api.v1.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FIleController {

    private final Response response;
    private final FileService fileServiceBasic;


    /**
     * 파일 업로드
     */
    @PostMapping("/upload")
    public ResponseEntity<Body> create(@RequestPart("file") MultipartFile multipartFile, @RequestParam String fileType) {
        FileUploadResponse fileUploadResponse = fileServiceBasic.upload(multipartFile, fileType);
        return response.success(Code.SAVE_SUCCESS, fileUploadResponse);
    }

}
