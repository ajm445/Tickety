package com.tickety.reservation.config;

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
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reservation Service API")
                        .version("1.0.0")
                        .description("Tickety 예약 서비스 API - 좌석 예약 및 관리")
                        .contact(new Contact()
                                .name("Tickety Team")
                                .email("support@tickety.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8083").description("Local Server"),
                        new Server().url("http://localhost:8080/api/reservations").description("Gateway Server")
                ));
    }
}
