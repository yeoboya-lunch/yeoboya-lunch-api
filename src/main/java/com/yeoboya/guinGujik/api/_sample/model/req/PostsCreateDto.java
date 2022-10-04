package com.yeoboya.guinGujik.api._sample.model.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
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


    public String getTitle(){
        // 아래와 같은 코드 서비스의 정책을 넣지마세요!!! 절대!!!
        return this.title.substring(0,10);
    }

    public PostsCreateDto changeTitle(String title){
        return PostsCreateDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}
