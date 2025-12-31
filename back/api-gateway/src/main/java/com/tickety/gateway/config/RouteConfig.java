package com.tickety.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("lb://auth-service"))
                .route("concert-service", r -> r
                        .path("/api/concerts/**")
                        .uri("lb://concert-service"))
                .route("reservation-service", r -> r
                        .path("/api/reservations/**")
                        .uri("lb://reservation-service"))
                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .uri("lb://payment-service"))
                .build();
    }
}
