package com.uptc.queenscorner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Queen's Corner Gallery API")
                        .description("API REST para el sistema de gestión de Queen's Corner Gallery")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Queen's Corner Gallery")
                                .email("soporte@queenscorner.com")));
    }
}