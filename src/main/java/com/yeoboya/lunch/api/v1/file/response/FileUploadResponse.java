package com.yeoboya.lunch.api.v1.file.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileUploadResponse {

    private String originalFileName;
    private String fileName;
    private String filePath;
    private String extension;
    private Long size;
}
