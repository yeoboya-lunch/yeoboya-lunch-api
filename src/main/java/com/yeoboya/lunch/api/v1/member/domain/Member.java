package com.yeoboya.lunch.api.v1.member.domain;

import com.yeoboya.lunch.api.v1.domain.BaseTimeEntity;
import com.yeoboya.lunch.config.security.constants.Authority;
import lombok.*;

import javax.persistence.*;

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

    @Enumerated(EnumType.STRING)
    private Authority role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Account account;

}
