package com.yeoboya.lunch.config.security.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "token_ignore_urls")
@Getter
@Setter
public class TokenIgnoreUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_ignore_id", nullable = false)
    private Long id;

    @Column(length = 255, nullable = false)
    private String url;

    @Column(name = "is_ignore", nullable = false)
    private Boolean isIgnore;

}
