package com.bix.imageprocessor.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        var contact = new Contact()
                .name("Gustavo Porto")
                .email("gustavofrancoporto@gmail.com");

        var info = new Info()
                .title("Image Transformation API")
                .description("This API provide endpoints to perform image transformations.")
                .version("v1")
                .contact(contact);

        var securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        var securityRequirement = new SecurityRequirement().addList(securityScheme.getName());

        var components = new Components().addSecuritySchemes(securityScheme.getName(), securityScheme);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}