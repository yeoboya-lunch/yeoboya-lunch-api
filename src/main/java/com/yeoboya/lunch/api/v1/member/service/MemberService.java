package com.yeoboya.lunch.api.v1.member.service;

import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.common.service.EmailService;
import com.yeoboya.lunch.api.v1.member.domain.Account;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.domain.MemberInfo;
import com.yeoboya.lunch.api.v1.member.repository.AccountRepository;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.api.v1.member.reqeust.*;
import com.yeoboya.lunch.api.v1.member.response.AccountResponse;
import com.yeoboya.lunch.api.v1.member.response.MemberProjections.MemberAccount;
import com.yeoboya.lunch.api.v1.member.response.MemberProjections.MemberSummary;
import com.yeoboya.lunch.api.v1.member.response.MemberResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;

    public MemberService(MemberRepository memberRepository, AccountRepository accountRepository, EmailService emailService) {
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
        this.emailService = emailService;
    }

    public List<MemberResponse> memberList(Pageable pageable) {
        return memberRepository.getMembers(pageable);
    }

    public List<MemberSummary> memberSummary(String email){
        return memberRepository.findByEmail(email, MemberSummary.class);
    }

    public List<MemberAccount> memberAccount(String email){
        return memberRepository.findByEmail(email, MemberAccount.class);
    }


    @Transactional
    public void editMemberInfo(String memberEmail, MemberInfoEdit memberInfoEdit) {
        MemberInfo memberInfo = memberRepository.getMemberInfo(memberEmail);

        MemberInfoEditor.MemberInfoEditorBuilder editorBuilder = memberInfo.toEditor();

        MemberInfoEditor memberInfoEditor = editorBuilder
                .bio(memberInfoEdit.getBio())
                .phoneNumber(memberInfoEdit.getPhoneNumber())
                .build();

        memberInfo.edit(memberInfoEditor);
    }

    public AccountResponse addAccount(AccountCreate accountCreate) {
        Member member = memberRepository.findByEmail(accountCreate.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Member not found - " + accountCreate.getEmail()));
        Account createAccount = Account.builder()
                .member(member)
                .bankName(accountCreate.getBankName())
                .accountNumber(accountCreate.getAccountNumber())
                .build();
        Account account = accountRepository.save(createAccount);
        return AccountResponse.from(account);
    }

    @Transactional
    public void editAccount(String memberEmail, AccountEdit edit) {
        Account account = accountRepository.findByMemberEmail(memberEmail).orElseThrow(
                () -> new EntityNotFoundException("Account not found - " + memberEmail));

        AccountEditor.AccountEditorBuilder editorBuilder = account.toEditor();

        AccountEditor accountEditor = editorBuilder
                .accountNumber(edit.getAccountNumber())
                .bankName(edit.getBankName())
                .build();

        account.edit(accountEditor);
    }


    public void sendResetPasswordMail(String memberEmail) {
        boolean existsByEmail = memberRepository.existsByEmail(memberEmail);

        if(!existsByEmail){
            throw new EntityNotFoundException("Member not found - " + memberEmail);
        }

        emailService.resetPassword(memberEmail);
    }
}