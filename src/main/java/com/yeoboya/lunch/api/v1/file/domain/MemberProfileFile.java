package com.yeoboya.lunch.api.v1.file.domain;


import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import com.yeoboya.lunch.api.v1.file.response.ProfileUploadResponse;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_PROFILE_FILE_ID", nullable = false)
    private Long id;

    private String originalFileName;

    private String fileName;

    private String filePath;

    private String externalForm;

    private String extension;

    private Long size;

    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public MemberProfileFile(Member member, ProfileUploadResponse profileUploadResponse) {
        this.member = member;
        this.originalFileName = profileUploadResponse.getOriginalFileName();
        this.fileName = profileUploadResponse.getFileName();
        this.filePath = profileUploadResponse.getFilePath();
        this.extension = profileUploadResponse.getExtension();
        this.externalForm = profileUploadResponse.getExternalForm();
        this.size = profileUploadResponse.getSize();
        this.isDefault = profileUploadResponse.getIsDefault();
    }


    public void saveMember(Member member) {
        this.member = member;
        if (!member.getMemberProfileFiles().contains(this)) {
            member.getMemberProfileFiles().add(this);
        }
    }

    @Override
    public String toString() {
        return "MemberProfileFile{" +
                "id=" + id +
                ", originalFileName='" + originalFileName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", externalForm='" + externalForm + '\'' +
                ", extension='" + extension + '\'' +
                ", size=" + size +
                ", member=" + member +
                '}';
    }
}
