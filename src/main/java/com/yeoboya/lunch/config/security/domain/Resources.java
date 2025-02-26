package com.yeoboya.lunch.config.security.domain;


import com.yeoboya.lunch.config.security.reqeust.ResourcesRequest;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "RESOURCE")
@Data
@ToString(exclude = {"roleSet"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resources implements Serializable {

    @Id
    @Column(name = "RESOURCES_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_name", unique = true)
    private String resourceName;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "order_num", nullable = true)
    private Integer orderNum;

    @Column(name = "resource_type")
    private String resourceType;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_resources",
            joinColumns = { @JoinColumn(name = "resource_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<Role> roleSet = new HashSet<>();

    public static Resources createResources(ResourcesRequest resourcesRequest){
        Resources resources = new Resources();
        resources.setResourceName(resourcesRequest.getResourceName());
        resources.setHttpMethod(resourcesRequest.getHttpMethod());
        resources.setOrderNum(resourcesRequest.getOrderNum());
        resources.setResourceType(resourcesRequest.getResourceType());
        return resources;
    }

}
