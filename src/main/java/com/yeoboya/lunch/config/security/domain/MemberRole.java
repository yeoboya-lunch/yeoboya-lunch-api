package com.yeoboya.lunch.config.security.domain;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_MEMBER_ROLE",
                        columnNames = {"MEMBER_ID", "ROLES_ID"}
                )
        }
)
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ROLES_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLES_ID")
    private Role role;

    public static MemberRole createMemberRoles(Member member, Role role) {
        MemberRole memberRole = new MemberRole();
        memberRole.setMember(member);
        memberRole.setRole(role);
        return memberRole;
    }

}
