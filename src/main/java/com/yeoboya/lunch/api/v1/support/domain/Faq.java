package com.yeoboya.lunch.api.v1.support.domain;

import com.yeoboya.lunch.api.v1.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Faq extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @Builder
    public Faq(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}
