package com.yeoboya.lunch.config.etc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Yeoboya-lunch API",
                version = "1.0",
                description = "Redoc을 사용한 API 문서",
                contact = @Contact(
                        name = "Yeoboya Dev Team",
                        email = "support@yeoboya-lunch.com",
                        url = "https://yeoboya-lunch.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Server"),
                @Server(url = "https://api.yeoboya-lunch.com", description = "Production Server")
        },
        security = {
                @SecurityRequirement(name = "BearerAuth") // JWT 인증 설정
        },
        tags = {

        }
)
public class OpenAPIConfig {
}