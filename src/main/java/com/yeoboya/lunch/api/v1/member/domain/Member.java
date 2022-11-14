package com.yeoboya.lunch.api.v1.member.domain;

import com.yeoboya.lunch.api.v1.domain.BaseTimeEntity;
import com.yeoboya.lunch.config.security.dmain.MemberRole;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;

    @Column(unique = true)
    private String name;
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberRole> memberRoles = new ArrayList<>(); //권한 목록

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Account account;


    //연관관계 편의 메소드
    public static Member createMember(Member member, List<MemberRole> memberRoles){
        for(MemberRole roles : memberRoles) {
            member.addMemberRole(roles);
        }
        return member;
    }

    public void addMemberRole(MemberRole memberRole) {
        System.out.println("this.memberRoles" + this.memberRoles);
        this.memberRoles.add(memberRole);
        memberRole.setMember(this);
    }





}
