package com.yeoboya.guinGujik.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DocConfiguration {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info()
                .title("구인구직 REST API")
                .version(appVersion)
                .description("구인구직 REST API")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                        .name("여보야")
                        .url("http://www.yeoboyacorp.com/")
                        .email("khjzzm@gmail.com"))
                .license(new License()
                        .name("Apache License Version 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")
                );

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}