package com.yeoboya.lunch.config.security.response;

import com.yeoboya.lunch.config.security.domain.Resources;
import com.yeoboya.lunch.config.security.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ResourcesDTO {
    private Long id;
    private String resourceName;
    private String httpMethod;
    private int orderNum;
    private String resourceType;
    private String roleDesc;

//    List<String> order = Arrays.asList("어드민", "매니저", "유저", "테스터", "차단");

    public ResourcesDTO(Resources resources) {
        this.id = resources.getId();
        this.resourceName = resources.getResourceName();
        this.httpMethod = resources.getHttpMethod();
        this.orderNum = resources.getOrderNum();
        this.resourceType = resources.getResourceType();
        this.roleDesc = resources.getRoleSet().stream().map(Role::getRoleDesc).collect(Collectors.joining());
    }
}
