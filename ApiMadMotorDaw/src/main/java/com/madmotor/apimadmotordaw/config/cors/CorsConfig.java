package com.madmotor.apimadmotordaw.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/rest/categorias/**")
                        .allowedMethods("*")
                        .maxAge(3600);
                registry.addMapping("/rest/clientes/**")
                        .allowedMethods("*")
                        .maxAge(3600);
                registry.addMapping("/rest/pedidos/**")
                        .allowedMethods("*")
                        .maxAge(3600);
                registry.addMapping("/rest/personal/**")
                        .allowedMethods("*")
                        .maxAge(3600);
                registry.addMapping("/rest/piezas/**")
                        .allowedMethods("*")
                        .maxAge(3600);
                registry.addMapping("/rest/storage/**")
                        .allowedMethods("*")
                        .maxAge(3600);
                registry.addMapping("/rest/vehiculos/**")
                        .allowedMethods("*")
                        .maxAge(3600);
            }
        };
    }
}