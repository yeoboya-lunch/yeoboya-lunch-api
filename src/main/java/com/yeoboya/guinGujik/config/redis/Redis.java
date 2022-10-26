package com.yeoboya.guinGujik.config.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.redis")
@Configuration
public class Redis {
    private String host;
    private int port;
    private String password;
    private int database;
    private Redis master;
    private List<Redis> slaves;
}