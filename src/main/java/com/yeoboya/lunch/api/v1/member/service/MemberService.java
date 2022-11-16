package com.yeoboya.lunch.api.v1.member.service;

import com.yeoboya.lunch.api.v1.exception.AccountNotFound;
import com.yeoboya.lunch.api.v1.exception.MemberNotFound;
import com.yeoboya.lunch.api.v1.member.domain.Account;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.AccountRepository;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountCreate;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountEdit;
import com.yeoboya.lunch.api.v1.member.reqeust.AccountEditor;
import com.yeoboya.lunch.api.v1.member.response.MemberResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    public MemberService(MemberRepository memberRepository, AccountRepository accountRepository) {
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
    }

    public List<MemberResponse> memberList(Pageable pageable) {
        return memberRepository.getMembers(pageable).stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());
    }

    public void addAccount(AccountCreate accountCreate) {
        Member member = memberRepository.findByName(accountCreate.getName()).orElseThrow(MemberNotFound::new);
        Account createAccount = Account.builder()
                .member(member)
                .bankName(accountCreate.getBankName())
                .accountNumber(accountCreate.getAccountNumber())
                .build();
        accountRepository.save(createAccount);
    }

    @Transactional
    public void editAccount(String memberName, AccountEdit edit) {
        Account account = accountRepository.findByMemberName(memberName).orElseThrow(AccountNotFound::new);

        AccountEditor.AccountEditorBuilder editorBuilder = account.toEditor();

        AccountEditor accountEditor = editorBuilder.accountNumber(edit.getAccountNumber())
                .bankName(edit.getBankName())
                .build();

        account.edit(accountEditor);
    }
}
