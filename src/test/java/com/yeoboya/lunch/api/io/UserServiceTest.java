package com.yeoboya.lunch.api.io;

import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import com.yeoboya.lunch.config.security.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.stream.IntStream;

@SpringBootTest
@WithMockUser(username = "kimhyunjin@outlook.kr", roles = "USER")
public class UserServiceTest {


    @Autowired
    UserService service;

    @Test
    void signUp() {
        UserRequest.SignUp signUp = new UserRequest.SignUp();
        IntStream.rangeClosed(100, 300).forEach(i->{
            signUp.setEmail(i+"@"+i+"."+i);
            signUp.setName("happy"+i);
            signUp.setPassword("qwer1234@@");
            service.signUp(signUp);
        });

    }
}
