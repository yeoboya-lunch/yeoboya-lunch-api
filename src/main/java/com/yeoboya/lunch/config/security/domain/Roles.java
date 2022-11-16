package com.yeoboya.lunch.config.security.domain;

import com.yeoboya.lunch.config.security.constants.Authority;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Roles  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLES_ID", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Authority role;

    @Builder
    public Roles(Authority role) {
        this.role = role;
    }

}
