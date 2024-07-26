package com.yeoboya.lunch.api.v1.file.service;

import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import com.yeoboya.lunch.api.v1.file.response.ProfileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Function;

public interface FileService {

    String makeNewDirectory(String subDirector);
    String makeNewFileName(String fileExtension);
    boolean validationExtension(String filenameExtension);
//    FileUploadResponse upload(MultipartFile multipartFile, String fileType) throws IOException;
//    ProfileUploadResponse upload(MultipartFile multipartFile, String fileType) throws IOException;

    <T extends FileUploadResponse> T upload(MultipartFile multipartFile, String fileType, Function<FileUploadResponse, T> mapper) throws IOException;
}

