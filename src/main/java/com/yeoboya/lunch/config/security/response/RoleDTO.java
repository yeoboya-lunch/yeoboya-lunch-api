package com.yeoboya.lunch.config.security.response;

import com.yeoboya.lunch.config.security.domain.Role;
import com.yeoboya.lunch.config.security.domain.Resources;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RoleDTO {
    private Long id;
    private AuthorityDTO role;
    private String roleDesc;
    private Set<Long> resourcesIds;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.role = new AuthorityDTO(role.getRole());
        this.roleDesc = role.getRoleDesc();
        this.resourcesIds = role.getResourcesSet().stream().map(Resources::getId).collect(Collectors.toSet());
    }
}
