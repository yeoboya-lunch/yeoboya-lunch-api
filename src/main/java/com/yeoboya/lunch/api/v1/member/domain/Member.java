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
    @Column(name = "MEMBER_ID", nullable = false)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberRole> memberRoles = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private Account account;

    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
    private MemberInfo memberInfo;


    //연관관계 편의 메소드
    public static Member createMember(Member pMember, MemberInfo memberInfo, List<MemberRole> memberRoles){
        Member member = new Member();
        member.setEmail(pMember.getEmail());
        member.setName(pMember.getName());
        member.setPassword(pMember.getPassword());
        member.addMemberInfo(memberInfo);
        for(MemberRole roles : memberRoles) {
            member.addMemberRole(roles);
        }
        return member;
    }

    public void addMemberRole(MemberRole memberRole) {
        this.memberRoles.add(memberRole);
        memberRole.setMember(this);
    }

    public void addMemberInfo(MemberInfo memberInfo){
        this.setMemberInfo(memberInfo);
        memberInfo.setMember(this);
    }
}