# Tickety 백엔드 - MSA 아키텍처

## 프로젝트 구조

```
back/
├── eureka-server/         # 서비스 디스커버리 (포트: 8761)
├── api-gateway/           # API 게이트웨이 (포트: 8080)
├── auth-service/          # 인증 서비스 (포트: 8081) - 예정
├── concert-service/       # 공연 관리 서비스 (포트: 8082) - 예정
├── reservation-service/   # 예약 관리 서비스 (포트: 8083)
└── payment-service/       # 결제 서비스 (포트: 8084) - 예정
```

## 사전 요구사항

- Java 17+
- Gradle 8.x
- Docker (선택사항, Kafka 사용 시)

## 빠른 시작

### 1. Eureka Server 시작
```bash
cd eureka-server
./gradlew bootRun
```
Eureka 대시보드: http://localhost:8761

### 2. API Gateway 시작
```bash
cd api-gateway
./gradlew bootRun
```

### 3. Reservation Service 시작
```bash
cd reservation-service
./gradlew bootRun
```
- Swagger UI: http://localhost:8083/swagger-ui.html
- H2 콘솔: http://localhost:8083/h2-console

## API 엔드포인트

### 예약 서비스 (Reservation Service)

| 메서드 | 엔드포인트 | 설명 |
|--------|----------|-------------|
| POST | `/api/reservations` | 예약 생성 |
| GET | `/api/reservations` | 사용자 예약 목록 조회 |
| GET | `/api/reservations/{id}` | 예약 상세 조회 |
| DELETE | `/api/reservations/{id}` | 예약 취소 |
| POST | `/api/reservations/{id}/confirm` | 예약 확정 |
| GET | `/api/reservations/seats/concert/{id}` | 공연별 좌석 조회 |

## 환경 변수 설정

`.env.example`을 `.env`로 복사하고 설정하세요:

```bash
cp .env.example .env
```

## Supabase 연동

프로덕션 환경에서는 `supabase` 프로필을 사용하세요:

```bash
SPRING_PROFILES_ACTIVE=supabase ./gradlew bootRun
```

## 테스트 실행

```bash
cd reservation-service
./gradlew test
```

### 동시성 테스트

동시성 테스트는 100명의 사용자가 동시에 같은 좌석을 예약하는 상황을 시뮬레이션합니다.
비관적 락(Pessimistic Lock)으로 인해 단 1건의 예약만 성공해야 합니다.

```bash
./gradlew test --tests "*ConcurrencyTest*"
```

## 기술 스택

- Spring Boot 3.3.x
- Spring Cloud 2023.0.x
- Spring Data JPA
- PostgreSQL (Supabase)
- H2 Database (개발용)
- Springdoc OpenAPI 2.3.x
