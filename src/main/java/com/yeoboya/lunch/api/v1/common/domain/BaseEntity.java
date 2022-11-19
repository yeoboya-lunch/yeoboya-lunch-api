package com.yeoboya.lunch.api.v1.common.domain;


import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity extends BaseTimeEntity {

    @Column(updatable = false)
    @CreatedBy
    protected String createBy;

    @LastModifiedBy
    protected String lastModifiedBy;


}