package com.yeoboya.lunch.api.v1.file.response;

import com.yeoboya.lunch.api.v1.file.domain.File;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FileUploadResponse {

    private String originalFileName;
    private String fileName;
    private String filePath;
    private String extension;
    private Long size;

    public static List<FileUploadResponse> from(List<File> files) {
        return null;
    }
}
