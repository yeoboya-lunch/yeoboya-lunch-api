package com.yeoboya.lunch.api.v1.file.response;

import com.yeoboya.lunch.api.v1.file.domain.MemberProfileFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUploadResponse extends FileUploadResponse{

    private Long imageNo;
    private Boolean isDefault;

    public static ProfileUploadResponse from(MemberProfileFile files) {
        ProfileUploadResponse profileUploadResponse = new ProfileUploadResponse();
        profileUploadResponse.setImageNo(files.getId());
        profileUploadResponse.setOriginalFileName(files.getOriginalFileName());
        profileUploadResponse.setFileName(files.getFileName());
        profileUploadResponse.setFilePath(files.getFilePath());
        profileUploadResponse.setExtension(files.getExtension());
        profileUploadResponse.setExternalForm(files.getExternalForm());
        profileUploadResponse.setSize(files.getSize());
        profileUploadResponse.setUrl("https://yeoboya-lunch-s3-bucket.s3.ap-northeast-2.amazonaws.com/" + files.getFilePath() + "/" + files.getFileName());
        profileUploadResponse.setIsDefault(files.getIsDefault());
        return profileUploadResponse;
    }
}
