package com.yeoboya.lunch.config.security.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleResource {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROLES_ID")
    private Roles roles;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RESOURCE_ID")
    private Resources resources;
}
