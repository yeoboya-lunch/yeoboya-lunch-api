package com.yeoboya.lunch.api.v2.dalla.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@NoArgsConstructor
@ConfigurationProperties(prefix = "dalla")
public class DallaPayload {
    private String baseUrl;
    private String authToken;
    private String memNo;
    private String socketUrl;
}
