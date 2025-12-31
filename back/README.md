# Tickety 백엔드 - MSA 아키텍처

Spring Boot 기반 마이크로서비스 아키텍처의 티켓팅 플랫폼 백엔드

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

## 구현 상태

### 완료된 서비스
- [x] **Eureka Server** - 서비스 레지스트리
- [x] **API Gateway** - 라우팅, 인증, CORS
- [x] **Reservation Service** - 예약 및 좌석 관리

### 미구현 서비스
- [ ] **Auth Service** - 로그인, 회원가입, JWT 발급
- [ ] **Concert Service** - 공연 관리
- [ ] **Payment Service** - 결제 처리

## 사전 요구사항

- Java 17+
- Gradle 8.x
- Docker (선택사항, Kafka 사용 시)

## 빠른 시작

### 1. 환경 변수 설정

```bash
cp .env.example .env
# .env 파일을 열어 실제 값으로 수정
```

### 2. Eureka Server 시작
```bash
cd eureka-server
./gradlew bootRun
```
Eureka 대시보드: http://localhost:8761

### 3. API Gateway 시작
```bash
cd api-gateway
./gradlew bootRun
```

### 4. Reservation Service 시작
```bash
cd reservation-service
./gradlew bootRun
```
- Swagger UI: http://localhost:8083/swagger-ui.html
- H2 콘솔: http://localhost:8083/h2-console

## API 엔드포인트

### 예약 서비스 (Reservation Service)

| 메서드 | 엔드포인트 | 설명 | 인증 |
|--------|----------|-------------|------|
| POST | `/api/reservations` | 예약 생성 | O |
| GET | `/api/reservations` | 사용자 예약 목록 조회 | O |
| GET | `/api/reservations/{id}` | 예약 상세 조회 | O |
| DELETE | `/api/reservations/{id}` | 예약 취소 | O |
| POST | `/api/reservations/{id}/confirm` | 예약 확정 | O |
| GET | `/api/reservations/seats/concert/{id}` | 공연별 좌석 조회 | O |
| GET | `/api/reservations/seats/concert/{id}/available` | 예약 가능 좌석 조회 | O |
| POST | `/api/reservations/seats/{id}/hold` | 좌석 임시 점유 | O |
| DELETE | `/api/reservations/seats/{id}/hold` | 좌석 점유 해제 | O |

### 공연 서비스 (Concert Service) - 예정

| 메서드 | 엔드포인트 | 설명 | 인증 |
|--------|----------|-------------|------|
| GET | `/api/concerts` | 공연 목록 조회 | X |
| GET | `/api/concerts/{id}` | 공연 상세 조회 | X |
| POST | `/api/concerts` | 공연 등록 | O (관리자) |

### 인증 서비스 (Auth Service) - 예정

| 메서드 | 엔드포인트 | 설명 | 인증 |
|--------|----------|-------------|------|
| POST | `/api/auth/login` | 로그인 | X |
| POST | `/api/auth/signup` | 회원가입 | X |
| POST | `/api/auth/refresh` | 토큰 갱신 | X |
| GET | `/api/auth/me` | 내 정보 조회 | O |

## 보안 설정

### API Gateway 인증

API Gateway에서 JWT 토큰을 검증하고 `X-User-Id` 헤더를 자동으로 설정합니다.

**인증 불필요 (Open Endpoints):**
- `POST /api/auth/login`
- `POST /api/auth/signup`
- `POST /api/auth/refresh`
- `GET /api/concerts/**` (조회만)

**인증 필요:**
- `/api/reservations/**` (모든 예약 API)
- `/api/payments/**` (모든 결제 API)

### JWT 토큰 검증

```
Authorization: Bearer <jwt-token>
```

- 유효한 토큰 → userId 추출 → `X-User-Id` 헤더로 전달
- 잘못된 토큰 → 401 Unauthorized 응답

### 환경 변수

| 변수 | 설명 | 필수 |
|------|------|------|
| `JWT_SECRET` | JWT 서명 키 (최소 32자) | O |
| `SUPABASE_PASSWORD` | DB 비밀번호 | O (프로덕션) |
| `SUPABASE_POOLER_HOST` | DB 호스트 | O (프로덕션) |

> **주의:** `.env` 파일은 `.gitignore`에 포함되어 있습니다. 민감한 정보를 커밋하지 마세요.

## 데이터베이스

### 개발 환경 (H2)

기본 프로필에서는 인메모리 H2 데이터베이스를 사용합니다.

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:h2:mem:reservationdb
```

### 프로덕션 환경 (Supabase PostgreSQL)

```bash
SPRING_PROFILES_ACTIVE=supabase ./gradlew bootRun
```

연결 풀 설정:
- 최대 연결 수: 10
- 최소 유휴 연결: 5
- 연결 타임아웃: 30초
- 유휴 타임아웃: 10분

## 엔티티 구조

### 주요 엔티티

| 엔티티 | 설명 | 주요 필드 |
|--------|------|----------|
| `Venue` | 공연장 | name, address, city, totalSeats |
| `Concert` | 공연 | title, artist, concertDate, status, priceMin/Max |
| `Seat` | 좌석 | section, rowNumber, seatNumber, grade, price, status |
| `Reservation` | 예약 | reservationNumber, status, totalAmount, expiresAt |
| `ReservationSeat` | 예약-좌석 매핑 | price (예약 시점 가격) |
| `Payment` | 결제 | paymentNumber, amount, method, status |

### 상태 값

**ReservationStatus:**
- `PENDING` - 결제 대기
- `CONFIRMED` - 예약 확정
- `CANCELLED` - 취소됨
- `EXPIRED` - 만료됨

**SeatStatus:**
- `AVAILABLE` - 예약 가능
- `HELD` - 임시 점유 (5분)
- `RESERVED` - 예약됨
- `SOLD` - 판매 완료

**ConcertStatus:**
- `SCHEDULED` - 예정
- `OPEN` - 예매 중
- `SOLD_OUT` - 매진
- `CANCELLED` - 취소
- `COMPLETED` - 종료

## 동시성 제어

좌석 예약 시 **비관적 락(Pessimistic Lock)**을 사용하여 동시성 문제를 해결합니다.

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT s FROM Seat s WHERE s.id IN :ids")
List<Seat> findAllByIdWithPessimisticLock(@Param("ids") List<UUID> ids);
```

### 동시성 테스트

100명의 사용자가 동시에 같은 좌석을 예약하면 1건만 성공해야 합니다.

```bash
./gradlew test --tests "*ConcurrencyTest*"
```

## 스케줄 태스크

### 만료된 예약 처리

매 1분마다 만료된 예약을 자동으로 처리합니다.

```java
@Scheduled(fixedRate = 60000)
public void processExpiredReservations() {
    // PENDING 상태이며 expiresAt이 지난 예약 → EXPIRED로 변경
    // 예약된 좌석 → AVAILABLE로 해제
}
```

## 테스트 실행

```bash
cd reservation-service

# 전체 테스트
./gradlew test

# 동시성 테스트만
./gradlew test --tests "*ConcurrencyTest*"

# 서비스 테스트만
./gradlew test --tests "*ServiceTest*"
```

## 기술 스택

| 분류 | 기술 |
|------|------|
| 프레임워크 | Spring Boot 3.3.x |
| 클라우드 | Spring Cloud 2023.0.x |
| 데이터베이스 | PostgreSQL (Supabase), H2 (개발) |
| ORM | Spring Data JPA, Hibernate |
| API 문서 | Springdoc OpenAPI 2.3.x |
| 인증 | JWT (jjwt 0.12.5) |
| 빌드 | Gradle 8.x (Kotlin DSL) |

## 개선 예정 사항

### 높은 우선순위
- [ ] Auth Service 구현 (JWT 발급)
- [ ] Concert Service 구현
- [ ] Payment Service 구현

### 중간 우선순위
- [ ] 예약 목록 Pagination 추가
- [ ] 캐싱 전략 수립 (Redis)
- [ ] 서비스 간 통신 (Feign Client)

### 낮은 우선순위
- [ ] Kafka 이벤트 드리븐 아키텍처
- [ ] 모니터링 (Prometheus, Grafana)
- [ ] 분산 트레이싱 (Zipkin)
