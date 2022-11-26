package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.api.v1.common.response.Body;
import com.yeoboya.lunch.config.security.reqeust.UserRequest.*;
import com.yeoboya.lunch.config.security.service.UsersService;
import com.yeoboya.lunch.config.security.validation.ValidationGroups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUp")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Body> signUp(@Valid @RequestBody SignUp signUp) {
        return usersService.signUp(signUp);
    }

    /**
     * 로그인
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignIn signIn) {
        return usersService.signIn(signIn);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/sign-out")
    public ResponseEntity<Body> signOut(@Valid @RequestBody SignOut signOut) {
        return usersService.signOut(signOut);
    }

    /**
     * 비밀번호 변경
     */
    @PostMapping("/setting/security")
    public ResponseEntity<Body> changePassword(@Validated(ValidationGroups.KnowOldPassword.class) @RequestBody Password password){
        return usersService.changePassword(password);
    }

    /**
     * 비밀번호 초기화
     * 메일->비밀번호 변경 페이지?
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<Body> resetPassword(@Validated(ValidationGroups.UmKnowOldPassword.class) @RequestBody Password password){
        return usersService.resetPassword(password);
    }


    /**
     * 토큰 재발급
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Validated @RequestBody Reissue reissue) {
        return usersService.reIssue(reissue);
    }

    /**
     * 권한추가
     */
    @GetMapping("/authority")
    public ResponseEntity<?> authority(HttpServletRequest request) {
        return usersService.authority(request);
    }

}