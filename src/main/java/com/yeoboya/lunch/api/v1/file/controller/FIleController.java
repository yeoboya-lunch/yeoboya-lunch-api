package com.yeoboya.lunch.api.v1.file.controller;

import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import com.yeoboya.lunch.api.v1.file.service.FileServiceBasic;
import com.yeoboya.lunch.api.v1.file.service.FileServiceS3;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FIleController {

    private final Response response;
    private final FileServiceS3 fileServiceS3;
    private final FileServiceBasic fileServiceBasic;

    /**
     * 파일 업로드
     */
    @PostMapping("/basic-upload")
    public ResponseEntity<Body> create(@RequestPart("file") MultipartFile multipartFile, @RequestParam String fileType) {
        FileUploadResponse fileUploadResponse = fileServiceBasic.upload(multipartFile, fileType);
        return response.success(Code.SAVE_SUCCESS, fileUploadResponse);
    }

    /**
     * AWS 파일 업로드
     */
    @PostMapping("/s3-upload")
    public ResponseEntity<Body> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String subDirectory) throws IOException {
        FileUploadResponse upload = fileServiceS3.upload(file, subDirectory, null);
        return response.success(Code.SAVE_SUCCESS, upload);
    }

    @GetMapping("/s3")
    public ResponseEntity<Body> readFile() {
        return response.success(Code.SAVE_SUCCESS, fileServiceS3.read());
    }
}
