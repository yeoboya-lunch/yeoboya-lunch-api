package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.config.common.Response;
import com.yeoboya.lunch.config.security.reqeust.UserRequest;
import com.yeoboya.lunch.config.security.service.UsersService;
import com.yeoboya.lunch.config.util.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;
    private final Response response;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Validated @RequestBody UserRequest.SignUp signUp, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.signUp(signUp);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Validated @RequestBody UserRequest.SignIn signIn, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.signIn(signIn);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@Validated @RequestBody UserRequest.SignOut signOut, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.signOut(signOut);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Validated @RequestBody UserRequest.Reissue reissue, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.reIssue(reissue);
    }

    @GetMapping("/authority")
    public ResponseEntity<?> authority(HttpServletRequest request) {
        return usersService.authority(request);
    }

}