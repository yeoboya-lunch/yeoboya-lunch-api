package com.yeoboya.lunch.api.v1.support.domain;

import com.yeoboya.lunch.api.v1.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INQUIRY_ID", nullable = false)
    private Long id;

    @Column
    private String loginId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String content;

    @Builder
    public Inquiry(String loginId,String email, String subject, String content) {
        this.loginId = loginId;
        this.email = email;
        this.subject = subject;
        this.content = content;
    }
}
