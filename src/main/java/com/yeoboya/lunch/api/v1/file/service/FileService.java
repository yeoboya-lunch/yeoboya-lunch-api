package com.yeoboya.lunch.api.v1.file.service;

import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String makeNewDirectory(String subDirector);
    String makeNewFileName(String fileExtension);
    boolean validationExtension(String filenameExtension);
    FileUploadResponse upload(MultipartFile multipartFile, String fileType) throws IOException;

}
