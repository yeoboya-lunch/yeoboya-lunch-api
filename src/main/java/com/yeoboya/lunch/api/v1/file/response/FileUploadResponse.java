package com.yeoboya.lunch.api.v1.file.response;

import com.yeoboya.lunch.api.v1.file.domain.BoardFile;
import com.yeoboya.lunch.api.v1.file.domain.MemberProfileFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileUploadResponse {

    @Builder
    public FileUploadResponse(String originalFileName, String fileName, String filePath, String extension, String externalForm, Long size) {
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.extension = extension;
        this.externalForm = externalForm;
        this.size = size;
    }

    private String originalFileName;
    private String fileName;
    private String filePath;
    private String extension;
    private String externalForm;
    private Long size;
    private String url;


    public static FileUploadResponse from(BoardFile files) {
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        fileUploadResponse.setOriginalFileName(files.getOriginalFileName());
        fileUploadResponse.setFileName(files.getFileName());
        fileUploadResponse.setFilePath(files.getFilePath());
        fileUploadResponse.setExtension(files.getExtension());
        fileUploadResponse.setSize(files.getSize());
        fileUploadResponse.setUrl("https://yeoboya-lunch-s3-bucket.s3.ap-northeast-2.amazonaws.com/" + files.getFilePath() + "/" + files.getFileName());
        return fileUploadResponse;
    }


}
