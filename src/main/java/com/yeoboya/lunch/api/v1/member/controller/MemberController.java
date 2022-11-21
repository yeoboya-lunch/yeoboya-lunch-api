package com.yeoboya.lunch.api.v1.member.controller;

import com.yeoboya.lunch.api.v1.common.response.Body;
import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountCreate;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountEdit;
import com.yeoboya.lunch.api.v1.member.response.AccountResponse;
import com.yeoboya.lunch.api.v1.member.response.MemberResponse;
import com.yeoboya.lunch.api.v1.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final Response response;
    private final MemberService memberService;

    /**
     * 멤버 리스트
     */
    @GetMapping
    public ResponseEntity<Body> member(Pageable pageable) {
        List<MemberResponse> memberResponses = memberService.memberList(pageable);
        return response.success(memberResponses, Code.SEARCH_SUCCESS);
    }

    /**
     * 멤버 계좌 등록
     */
    @PostMapping("/account")
    public ResponseEntity<Body> account(@RequestBody @Valid AccountCreate accountCreate) {
        AccountResponse accountResponse = memberService.addAccount(accountCreate);
        return response.success(accountResponse, Code.SAVE_SUCCESS);
    }

    /**
     * 멤버 계좌 수정
     */
    @PatchMapping("/account/{memberName}")
    public ResponseEntity<Body> accountUpdate(@PathVariable String memberName, @RequestBody @Valid AccountEdit edit){
        memberService.editAccount(memberName, edit);
        return response.success(Code.UPDATE_SUCCESS.getMsg());
    }
}