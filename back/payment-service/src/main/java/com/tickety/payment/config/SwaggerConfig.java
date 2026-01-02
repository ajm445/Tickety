package com.tickety.payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tickety Payment Service API")
                        .description("결제 관리 서비스 API")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Tickety Team")
                                .email("support@tickety.com")));
    }
}
