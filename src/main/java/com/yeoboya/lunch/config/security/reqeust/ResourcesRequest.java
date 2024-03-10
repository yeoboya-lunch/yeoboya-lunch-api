package com.yeoboya.lunch.config.security.reqeust;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourcesRequest {

    private String resourceName;
    private String httpMethod;
    private int orderNum;
    private String resourceType;
    private Long role;

}
