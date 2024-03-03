package com.yeoboya.lunch.config.security.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "RESOURCE_ID", nullable = false)
    private Long id;

    @Column(name = "HTTP_METHOD")
    private String httpMethod;

    @Column(name = "ORDER_NUM")
    private Integer orderNum;

    @Column(name = "RESOURCE_NAME")
    private String resourceName;

    @Column(name = "RESOURCE_TYPE")
    private String resourceType;

    @OneToMany(mappedBy = "resources", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleResource> roleResources = new HashSet<>();
}
