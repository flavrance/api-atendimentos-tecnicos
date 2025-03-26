package com.atendimentos.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        String localServerUrl = String.format("http://localhost:%s", serverPort);
        
        return new OpenAPI()
            .info(new Info()
                .title(applicationName)
                .version("1.0.0")
                .description("API REST para gerenciamento de atendimentos técnicos")
                .contact(new Contact()
                    .name("Time de Desenvolvimento")
                    .email("dev@empresa.com")
                    .url("https://github.com/seu-usuario/api-atendimentos-tecnicos"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url(localServerUrl)
                    .description("Servidor Local")));
    }
} 