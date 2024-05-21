package com.yeoboya.lunch.api.v1.member.domain;

import com.yeoboya.lunch.api.v1.common.domain.BaseTimeEntity;
import com.yeoboya.lunch.api.v1.file.domain.BoardFile;
import com.yeoboya.lunch.api.v1.file.domain.MemberProfileFile;
import com.yeoboya.lunch.config.pricingPlan.domain.ApiKey;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import com.yeoboya.lunch.config.security.domain.UserSecurityStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<MemberRole> memberRoles = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private Account account;

    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
    private MemberInfo memberInfo;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<LoginInfo> loginInfos;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<MemberProfileFile> memberProfileFiles = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private ApiKey apiKey;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private UserSecurityStatus userSecurityStatus;

    //연관관계 편의 메소드
    public static Member createMember(Member pMember, MemberInfo memberInfo, List<MemberRole> memberRoles,
                                      UserSecurityStatus userSecurityStatus){
        Member member = new Member();
        member.setEmail(pMember.getEmail());
        member.setName(pMember.getName());
        member.setPassword(pMember.getPassword());
        member.addMemberInfo(memberInfo);
        for(MemberRole roles : memberRoles) {
            member.addMemberRole(roles);
        }
        member.addUserSecurityStatus(userSecurityStatus);
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

    public void addUserSecurityStatus(UserSecurityStatus userSecurityStatus){
        this.setUserSecurityStatus(userSecurityStatus);
        userSecurityStatus.setMember(this);
    }

    @Override
    public String toString() {
        return "Member{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
