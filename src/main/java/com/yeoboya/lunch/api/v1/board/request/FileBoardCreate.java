package com.yeoboya.lunch.api.v1.board.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class FileBoardCreate extends BoardCreate {
    private String uploadType;
    private MultipartFile file;

    @Override
    public String toString() {
        return "FileBoardCreate{" +
                "uploadType='" + uploadType + '\'' +
                ", file=" + file + '\'' +
                ", super=" + super.toString() +
                '}';
    }
}
