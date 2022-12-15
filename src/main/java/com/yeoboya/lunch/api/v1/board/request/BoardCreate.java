package com.yeoboya.lunch.api.v1.board.request;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import java.util.List;

@Setter
@Getter
@ToString
public class BoardCreate {

    @Email
    private String email;

    private String title;             //제목

    private List<String> hashTag;     //해시태그

    private String content;           //콘텐츠

    @Digits(integer = 4, fraction = 0)
    private int pin;                  //비밀번호

    private boolean secret;           //비밀글여부

}
