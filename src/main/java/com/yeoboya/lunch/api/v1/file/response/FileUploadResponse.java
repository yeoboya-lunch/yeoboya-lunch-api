package com.yeoboya.lunch.api.v1.file.response;

import com.yeoboya.lunch.api.v1.file.domain.File;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileUploadResponse {

    @Builder
    public FileUploadResponse(String originalFileName, String fileName, String filePath, String extension, Long size) {
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.extension = extension;
        this.size = size;
    }

    private String originalFileName;
    private String fileName;
    private String filePath;
    private String extension;
    private Long size;

    public static FileUploadResponse from(File files) {
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        fileUploadResponse.setOriginalFileName(files.getOriginalFileName());
        fileUploadResponse.setFileName(files.getFileName());
        fileUploadResponse.setFilePath(files.getFilePath());
        fileUploadResponse.setExtension(files.getExtension());
        fileUploadResponse.setSize(files.getSize());
        return fileUploadResponse;
    }
}
