package vn.gov.prison.secure.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger Configuration for Spring Boot 3.2.x
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI prisonSecureOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Prison-Secure API")
                .version("1.0.0")
                .description("Enterprise Prison Management System - RESTful API Documentation")
                .contact(new Contact()
                    .name("Prison Admin")
                    .email("admin@prison.gov.vn")));
    }
}
