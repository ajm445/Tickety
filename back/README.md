# Tickety Backend - MSA Architecture

## Project Structure

```
back/
├── eureka-server/         # Service Discovery (port: 8761)
├── api-gateway/           # API Gateway (port: 8080)
├── auth-service/          # Authentication (port: 8081) - TBD
├── concert-service/       # Concert Management (port: 8082) - TBD
├── reservation-service/   # Reservation Management (port: 8083)
└── payment-service/       # Payment Processing (port: 8084) - TBD
```

## Prerequisites

- Java 17+
- Gradle 8.x
- Docker (optional, for Kafka)

## Quick Start

### 1. Start Eureka Server
```bash
cd eureka-server
./gradlew bootRun
```
Access Eureka Dashboard: http://localhost:8761

### 2. Start API Gateway
```bash
cd api-gateway
./gradlew bootRun
```

### 3. Start Reservation Service
```bash
cd reservation-service
./gradlew bootRun
```
- Swagger UI: http://localhost:8083/swagger-ui.html
- H2 Console: http://localhost:8083/h2-console

## API Endpoints

### Reservation Service

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/reservations` | Create reservation |
| GET | `/api/reservations` | Get user reservations |
| GET | `/api/reservations/{id}` | Get reservation detail |
| DELETE | `/api/reservations/{id}` | Cancel reservation |
| POST | `/api/reservations/{id}/confirm` | Confirm reservation |
| GET | `/api/reservations/seats/concert/{id}` | Get seats by concert |

## Environment Variables

Copy `.env.example` to `.env` and configure:

```bash
cp .env.example .env
```

## Supabase Configuration

For production, use the `supabase` profile:

```bash
SPRING_PROFILES_ACTIVE=supabase ./gradlew bootRun
```

## Running Tests

```bash
cd reservation-service
./gradlew test
```

### Concurrency Test

The concurrency test simulates 100 users attempting to reserve the same seat simultaneously.
Only one reservation should succeed (Pessimistic Lock).

```bash
./gradlew test --tests "*ConcurrencyTest*"
```

## Technology Stack

- Spring Boot 3.3.x
- Spring Cloud 2023.0.x
- Spring Data JPA
- PostgreSQL (Supabase)
- H2 Database (Development)
- Springdoc OpenAPI 2.3.x
