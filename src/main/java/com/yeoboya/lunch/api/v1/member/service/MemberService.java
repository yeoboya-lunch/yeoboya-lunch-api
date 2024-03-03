package com.yeoboya.lunch.api.v1.member.service;

import com.yeoboya.lunch.api.v1.common.exception.EntityNotFoundException;
import com.yeoboya.lunch.api.v1.common.response.SlicePagination;
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
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    public MemberService(MemberRepository memberRepository, AccountRepository accountRepository) {
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Map<String, Object> memberList(Pageable pageable) {
        Slice<MemberResponse> membersInPages = memberRepository.findMembersInPages(pageable);

        SlicePagination slicePagination = SlicePagination.builder()
                .pageNo(membersInPages.getNumber() + 1)
                .size(membersInPages.getSize())
                .numberOfElements(membersInPages.getNumberOfElements())
                .isFirst(membersInPages.isFirst())
                .isLast(membersInPages.isLast())
                .hasNext(membersInPages.hasNext())
                .hasPrevious(membersInPages.hasPrevious())
                .build();

        return Map.of(
                "list", membersInPages.getContent(),
                "pagination", slicePagination);
    }

    public List<MemberSummary> memberSummary(String email){
        return memberRepository.findByEmail(email, MemberSummary.class);
    }

    public List<MemberAccount> memberAccount(String email){
        return memberRepository.findByEmail(email, MemberAccount.class);
    }


    @Transactional
    public MemberResponse memberProfile(String memberEmail) {
        MemberResponse memberResponse = memberRepository.memberProfile(memberEmail);
        if(StringUtils.hasText(memberResponse.getAccountNumber())){
            memberResponse.setAccount(true);
        }
        return memberResponse;
    }

    /**
     * editMemberInfo 메소드는 사용자가 자신의 프로필을 수정하는 요청을 처리합니다.
     *
     * @param memberEmail 사용자의 이메일. 이메일을 통해 특정 사용자를 식별합니다.
     * @param memberInfoEdit 사용자가 변경하려는 정보. bio, nickName, phoneNumber를 포함합니다.
     *
     * 수정 작업은 다음과 같은 순서로 처리됩니다.
     * 1. memberEmail을 통해 MemberRepository에서 특정 Member를 찾습니다.
     *    - Member가 없으면 EntityNotFoundException을 발생시킵니다.
     * 2. 해당 Member의 현재 정보를 가져옵니다.
     * 3. 가져온 Member 정보를 통해 MemberInfoEditorBuilder를 생성합니다.
     * 4. memberInfoEdit에 담긴 변경하려는 정보를 이용해 MemberInfoEditor를 구성하고, 새 정보로 Member를 업데이트합니다.
     */
    @Transactional
    public void editMemberInfo(String memberEmail, MemberInfoEdit memberInfoEdit) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(
                () -> new EntityNotFoundException("Member not found - " + memberEmail));

        MemberInfo memberInfo = memberRepository.getMemberInfo(member.getEmail());

        MemberInfoEditor.MemberInfoEditorBuilder editorBuilder = memberInfo.toEditor();

        MemberInfoEditor memberInfoEditor = editorBuilder
                .bio(memberInfoEdit.getBio())
                .phoneNumber(memberInfoEdit.getPhoneNumber())
                .build();

        try {
            memberInfo.edit(memberInfoEditor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        try {
            account.edit(accountEditor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

