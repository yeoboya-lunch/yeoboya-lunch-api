package com.yeoboya.lunch.config.security.dmain;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.config.security.constants.Authority;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROLES_ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;  //주문 회원

    @Enumerated(EnumType.STRING)
    private Authority role;


    @Builder
    public Roles(Member member, Authority role) {
        this.member = member;
        this.role = role;
    }
}
