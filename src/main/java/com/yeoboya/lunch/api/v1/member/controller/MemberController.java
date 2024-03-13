package com.yeoboya.lunch.api.v1.member.controller;

import com.yeoboya.lunch.api.v1.common.response.Code;
import com.yeoboya.lunch.api.v1.common.response.Response;
import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountCreate;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountEdit;
import com.yeoboya.lunch.api.v1.member.reqeust.MemberInfoEdit;
import com.yeoboya.lunch.api.v1.member.response.AccountResponse;
import com.yeoboya.lunch.api.v1.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final Response response;
    private final MemberService memberService;

    /**
     * 멤버 리스트
     */
    @GetMapping
    public ResponseEntity<Body> member(Pageable pageable) {
        return response.success(Code.SEARCH_SUCCESS, memberService.memberList(pageable));
    }

    @GetMapping("{memberEmail}/summary")
    public ResponseEntity<Body> getMemberSummary(@PathVariable String memberEmail) {
        return response.success(Code.SEARCH_SUCCESS, memberService.memberSummary(memberEmail));
    }

    @GetMapping("{memberEmail}/profile")
    public ResponseEntity<Body> getMemberProfile(@PathVariable String memberEmail) {
        return response.success(Code.SEARCH_SUCCESS, memberService.memberProfile(memberEmail));
    }

    /**
     * 멤버 정보 검색(기본/계좌)
     */
    @GetMapping("/account/{memberEmail}")
    public ResponseEntity<Body> findAccountMember(@PathVariable String memberEmail) {
        return response.success(Code.SEARCH_SUCCESS, memberService.memberAccount(memberEmail));
    }

    /**
     * 멤버 상세 정보 수정
     */
    @PatchMapping("/setting/info/{memberEmail}")
    public ResponseEntity<Body> editMemberInfo(@PathVariable String memberEmail, @RequestBody MemberInfoEdit memberInfoEdit) {
        memberService.editMemberInfo(memberEmail, memberInfoEdit);
        return response.success(Code.UPDATE_SUCCESS);
    }


    /**
     * 멤버 계좌 등록
     */
    @PostMapping("/account")
    public ResponseEntity<Body> account(@RequestBody @Valid AccountCreate accountCreate) {
        AccountResponse accountResponse = memberService.addAccount(accountCreate);
        return response.success(Code.SAVE_SUCCESS, accountResponse);
    }

    /**
     * 멤버 계좌 수정
     */
    @PatchMapping("/account/{memberEmail}")
    public ResponseEntity<Body> accountUpdate(@PathVariable String memberEmail, @RequestBody AccountEdit accountEdit) {
        memberService.editAccount(memberEmail, accountEdit);
        return response.success(Code.UPDATE_SUCCESS);
    }

}
