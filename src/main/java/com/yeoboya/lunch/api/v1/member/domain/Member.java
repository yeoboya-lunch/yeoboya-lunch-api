package com.yeoboya.lunch.api.v1.member.domain;

import com.yeoboya.lunch.api.v1.common.domain.BaseTimeEntity;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String email;

    private String name;

    private String password;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberRole> memberRoles = new ArrayList<>(); //권한 목록

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Account account;


    //연관관계 편의 메소드
    public static Member createMember(Member pMember, List<MemberRole> memberRoles){
        Member member = new Member();
        member.setEmail(pMember.getEmail());
        member.setName(pMember.getName());
        member.setPassword(pMember.getPassword());
        for(MemberRole roles : memberRoles) {
            member.addMemberRole(roles);
        }
        return member;
    }

    public void addMemberRole(MemberRole memberRole) {
        this.memberRoles.add(memberRole);
        memberRole.setMember(this);
    }

}