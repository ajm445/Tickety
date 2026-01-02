package com.tickety.gateway.filter;

import com.tickety.gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private static final String X_USER_ID_HEADER = "X-User-Id";

    // Endpoints that don't require authentication
    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/api/auth/login",
            "/api/auth/signup",
            "/api/auth/refresh"
    );

    // Endpoints with read-only access (GET only)
    private static final List<String> PUBLIC_READ_ENDPOINTS = List.of(
            "/api/concerts",
            "/api/venues"
    );

    private final JwtUtil jwtUtil;

    public AuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        String method = request.getMethod().name();

        log.debug("Processing request: {} {}", method, path);

        // Allow open endpoints without authentication
        if (isOpenEndpoint(path)) {
            log.debug("Open endpoint accessed: {}", path);
            return chain.filter(exchange);
        }

        // Allow GET requests to public read endpoints
        if (isPublicReadEndpoint(path) && "GET".equals(method)) {
            log.debug("Public read endpoint accessed: {}", path);
            return chain.filter(exchange);
        }

        // Check for Authorization header
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            log.warn("Missing Authorization header for: {} {}", method, path);
            return setUnauthorizedResponse(exchange, "Authorization header is required");
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header format for: {} {}", method, path);
            return setUnauthorizedResponse(exchange, "Invalid Authorization header format");
        }

        // Extract token
        String token = authHeader.substring(7);

        // Validate JWT token
        if (!jwtUtil.validateToken(token)) {
            log.warn("Invalid or expired JWT token for: {} {}", method, path);
            return setUnauthorizedResponse(exchange, "Invalid or expired token");
        }

        // Extract user ID from token
        UUID userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            log.warn("Could not extract user ID from token for: {} {}", method, path);
            return setUnauthorizedResponse(exchange, "Invalid token payload");
        }

        // Add X-User-Id header to the request
        ServerHttpRequest modifiedRequest = request.mutate()
                .header(X_USER_ID_HEADER, userId.toString())
                .build();

        log.debug("Authenticated user {} for: {} {}", userId, method, path);

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private boolean isOpenEndpoint(String path) {
        return OPEN_ENDPOINTS.stream()
                .anyMatch(endpoint -> path.equals(endpoint) || path.startsWith(endpoint + "/"));
    }

    private boolean isPublicReadEndpoint(String path) {
        return PUBLIC_READ_ENDPOINTS.stream()
                .anyMatch(endpoint -> path.equals(endpoint) || path.startsWith(endpoint + "/"));
    }

    private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        log.debug("Returning 401 Unauthorized: {}", message);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
