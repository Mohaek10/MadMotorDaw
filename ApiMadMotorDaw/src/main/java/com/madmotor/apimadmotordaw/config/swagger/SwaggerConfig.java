package com.madmotor.apimadmotordaw.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${api.version}")
    private String apiVersion;

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("API REST - Mad Motor Daw Spring DAW 2023-2024")
                                .version("1.0.0")
                                .description("API desarrollada para proyecto de clase de uso de SpringBoot- Desarrollada en Spring.")
                                .termsOfService("https://www.google.com/")


                                .contact(
                                        new Contact()
                                                .name("Rubén Fernández Pérez")
                                                .url("https://github.com/Rubenoide03")
                                                .name("Miguel Vicario Rubio")
                                                .url("https://github.com/miviru")
                                                .name("Joe Brandon Carrillo Lozano")
                                                .url("https://github.com/JBrandonCL")
                                                .name("Mohamed El Kasmi El Kaderi ")
                                                .url("https://github.com/Mohaek10")

                                )

                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Repositorio del Proyecto")
                                .url("https://github.com/Mohaek10/MadMotorDaw")
                )
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()));
    }

    @Bean
    GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("https")
                .pathsToMatch(
                        "/"+apiVersion+"/categorias/**",
                        "/"+apiVersion+"/clientes/**",
                        "/"+apiVersion+"/pedidos/**",
                        "/"+apiVersion+"/personal/**",
                        "/"+apiVersion+"/piezas/**",
                        "/"+apiVersion+"/storage/**",
                        "/"+apiVersion+"/vehiculos/**")
                .displayName("API Mad Motor Spring DAW 2023 - 2024")
                .build();
    }
}