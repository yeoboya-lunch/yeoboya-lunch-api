package com.yeoboya.lunch.config.security.domain;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ROLES_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ROLES_ID")
    private Roles roles;

    public static MemberRole createMemberRoles(Roles roles){
        MemberRole memberRole = new MemberRole();
        memberRole.setRoles(roles);
        return memberRole;
    }

}
