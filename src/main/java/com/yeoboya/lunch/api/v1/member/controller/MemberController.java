package com.yeoboya.lunch.api.v1.member.controller;

import com.yeoboya.lunch.api.v1.member.reqeust.AccountCreate;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountEdit;
import com.yeoboya.lunch.api.v1.member.response.MemberResponse;
import com.yeoboya.lunch.api.v1.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public List<MemberResponse> member(Pageable pageable) {
        return memberService.memberList(pageable);
    }

    @PostMapping("/account")
    public void account(@RequestBody @Valid AccountCreate accountCreate) {
        memberService.addAccount(accountCreate);
    }

    @PatchMapping("/account/{memberName}")
    public void accountUpdate(@PathVariable String memberName, @RequestBody @Valid AccountEdit edit){
        memberService.editAccount(memberName, edit);
    }
}
