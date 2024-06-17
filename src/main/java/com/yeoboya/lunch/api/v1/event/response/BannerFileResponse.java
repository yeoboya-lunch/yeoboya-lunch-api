package com.yeoboya.lunch.api.v1.event.response;

import lombok.Data;

@Data
public class BannerFileResponse {
    private Long id;
    private String originalFileName;
    private String fileName;
    private String filePath;
    private String extension;
    private Long size;
    private String url;

    public BannerFileResponse(Long id, String originalFileName, String fileName, String filePath, String extension, Long size) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.extension = extension;
        this.size = size;
        this.url = "https://yeoboya-lunch-s3-bucket.s3.ap-northeast-2.amazonaws.com/" + filePath + "/" + fileName;
    }
}
