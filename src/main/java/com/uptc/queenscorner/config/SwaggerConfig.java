package com.uptc.queenscorner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Queen's Corner Gallery API")
                        .description("API REST para el sistema de gestión integral de Queen's Corner Gallery.\n\n" +
                                "Sistema de administración de:\n" +
                                "- Clientes\n" +
                                "- Cotizaciones y presupuestos\n" +
                                "- Negocios y proyectos\n" +
                                "- Facturas y pagos\n" +
                                "- Generación de PDFs asincrónica")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Queen's Corner Gallery")
                                .email("soporte@queenscorner.com")
                                .url("https://www.queenscorner.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token para autenticación")));
    }
} 