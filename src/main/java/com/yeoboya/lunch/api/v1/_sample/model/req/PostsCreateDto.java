package com.yeoboya.lunch.api.v1._sample.model.req;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PostsCreateDto {

    @Builder
    public PostsCreateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;

    //아래와 같은 서비스 정책을 setter 에 넣지마세요!!! 절대!!~
//    public String getTitle(){
//        return this.title.substring(0,10);
//    }

    public PostsCreateDto changeTitle(String title){
        return PostsCreateDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}
