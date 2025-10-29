package com.example.tingeso1.config;

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
                registry.addMapping("/**")
                        // 🔹 Aquí listamos los orígenes permitidos explícitamente
                        .allowedOriginPatterns(
                                "http://localhost:5173",   // Vite en local
                                "http://localhost:8080",   // Keycloak
                                "http://frontend",         // contenedor docker del front
                                "http://frontend:80"       // Nginx dentro de la red docker
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
