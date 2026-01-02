package com.tickety.concert.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Concert Service API")
                        .version("1.0.0")
                        .description("Tickety 공연 서비스 API")
                        .contact(new Contact()
                                .name("Tickety Team")
                                .email("support@tickety.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("Local Development"),
                        new Server().url("http://localhost:8080").description("API Gateway")
                ));
    }
}
