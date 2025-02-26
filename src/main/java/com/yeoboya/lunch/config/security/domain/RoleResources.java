package com.yeoboya.lunch.config.security.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ROLE_RESOURCES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ ID 자동 생성
    private Long id;

    @Column(name = "RESOURCE_ID", nullable = false)
    private Long resourceId;

    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;

    public RoleResources(Long resourceId, Long roleId) {
        this.resourceId = resourceId;
        this.roleId = roleId;
    }
}