package com.yeoboya.lunch.config.security.domain;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class UserSecurityStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_security_status_id", nullable = false)
    private Long id;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public static UserSecurityStatus createUserSecurityStatus(Member member) {
        UserSecurityStatus userSecurityStatus = new UserSecurityStatus();
        userSecurityStatus.setMember(member);
        return userSecurityStatus;
    }

}
