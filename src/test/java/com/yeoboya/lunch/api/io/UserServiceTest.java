package com.yeoboya.lunch.api.io;

import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import com.yeoboya.lunch.config.security.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
@WithMockUser(username = "kimhyunjin@outlook.kr", roles = "USER")
public class UserServiceTest {

    @Autowired
    UserService service;

    @Test
    @Transactional
    void signUp() {
        UserRequest.SignUp signUp = new UserRequest.SignUp();
        IntStream.rangeClosed(100, 300).forEach(i->{
            signUp.setLoginId("tester"+i);
            signUp.setEmail(i+"@yeoboya-lunch.com");
            signUp.setName("테스터"+i);
            signUp.setPassword("qwer1234@@");
            service.signUp(signUp);
        });

    }
}
