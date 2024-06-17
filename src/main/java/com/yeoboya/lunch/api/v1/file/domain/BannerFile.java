package com.yeoboya.lunch.api.v1.file.domain;


import com.yeoboya.lunch.api.v1.event.domain.Banner;
import com.yeoboya.lunch.api.v1.file.response.FileUploadResponse;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BANNER_FILE_ID", nullable = false)
    private Long id;

    private String originalFileName;

    private String fileName;

    private String filePath;

    private String extension;

    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BANNER_ID")
    private Banner banner;

    @Builder
    public BannerFile(Banner banner, FileUploadResponse fileUploadResponse) {
        this.banner = banner;
        this.originalFileName = fileUploadResponse.getOriginalFileName();
        this.fileName = fileUploadResponse.getFileName();
        this.filePath = fileUploadResponse.getFilePath();
        this.extension = fileUploadResponse.getExtension();
        this.size = fileUploadResponse.getSize();
    }

    public void setBoard(Banner banner) {
        this.banner = banner;
        if (!banner.getBannerFiles().contains(this)) {
            banner.getBannerFiles().add(this);
        }
    }
}
