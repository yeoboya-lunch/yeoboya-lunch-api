package com.yeoboya.lunch.config.security.dmain;

import com.yeoboya.lunch.api.v1.domain.BaseEntity;
import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class MemberRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ROLES_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Roles roles;


    public static MemberRole createMemberRoles(Roles roles){
        MemberRole memberRole = new MemberRole();
        memberRole.setRoles(roles);
        return memberRole;
    }


}
