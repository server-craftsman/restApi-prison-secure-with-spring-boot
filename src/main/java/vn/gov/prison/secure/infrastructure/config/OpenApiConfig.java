package vn.gov.prison.secure.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger Configuration for Spring Boot 3.2.x
 * Includes JWT Bearer Token Authentication and Custom Logo
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI prisonSecureOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enterprise Prison Management System - RESTful API")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
