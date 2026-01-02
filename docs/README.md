# Tickety - 티켓팅 플랫폼

MSA(Microservice Architecture) 기반의 콘서트 티켓 예약 플랫폼

## 프로젝트 개요

Tickety는 대규모 트래픽 환경에서 데이터 정합성을 보장하는 티켓팅 시스템입니다. 수만 명의 사용자가 동시에 같은 좌석을 예약하려 할 때 발생하는 동시성 문제를 비관적 락(Pessimistic Lock)으로 해결합니다.

### 핵심 기능
- 공연 정보 조회 및 좌석 선택
- 실시간 좌석 예약 (동시성 제어)
- API Gateway JWT 인증 필터
- 인증 보호 라우트 (프론트엔드)
- 결제 처리 (Saga 패턴) - 예정

---

## 프로젝트 구조

```
Tickety/
├── docs/                           # 프로젝트 문서
│   ├── README.md                   # 전체 프로젝트 가이드 (현재 파일)
│   ├── DOCS_REQUIREMENTS.md        # 요구사항 정의서
│   ├── DOCS_ARCHITECTURE.md        # 아키텍처 명세서
│   ├── DOCS_DB_DESIGN.md           # DB 설계 문서
│   └── CLAUDE_TASK_GUIDE.md        # 개발 작업 가이드
│
├── front/                          # 프론트엔드 (VSCode)
│   └── tickety-web/                # React + TypeScript + Vite
│
└── back/                           # 백엔드 (IntelliJ)
    ├── eureka-server/              # 서비스 디스커버리
    ├── api-gateway/                # API 게이트웨이
    ├── auth-service/               # 인증 서비스 ✅
    ├── concert-service/            # 공연 서비스 ✅
    ├── reservation-service/        # 예약 서비스 ✅
    └── payment-service/            # 결제 서비스 (예정)
```

---

## 기술 스택

### 백엔드
| 구분 | 기술 |
|------|------|
| Framework | Spring Boot 3.3.x |
| Cloud | Spring Cloud 2023.0.x |
| Language | Java 17 |
| Build Tool | Gradle 8.x (Kotlin DSL) |
| Database | PostgreSQL (Supabase) |
| ORM | Spring Data JPA |
| API 문서 | Springdoc OpenAPI 2.3.x |
| 테스트 | JUnit 5, Mockito |

### 프론트엔드
| 구분 | 기술 |
|------|------|
| Framework | React 19 + Vite |
| Language | TypeScript |
| 스타일링 | Tailwind CSS |
| 상태관리 | TanStack Query + Zustand |
| 라우팅 | React Router v7 |
| 폼 관리 | React Hook Form + Zod |
| 아이콘 | Lucide React |

---

## 마이크로서비스 구성

### 서비스 목록 및 포트

| 서비스 | 포트 | 설명 | 상태 |
|--------|------|------|------|
| Eureka Server | 8761 | 서비스 디스커버리 | ✅ 완료 |
| API Gateway | 8080 | 라우팅, CORS, JWT 인증 필터 | ✅ 완료 |
| Auth Service | 8081 | JWT 발급/인가 | ✅ 완료 |
| Concert Service | 8082 | 공연/공연장 관리 | ✅ 완료 |
| Reservation Service | 8083 | 좌석 예약 (비관적 락) | ✅ 완료 |
| Payment Service | 8084 | 결제 처리 (Saga) | 📋 예정 |
| Frontend | 5173 | React 웹 애플리케이션 | ✅ 완료 |

### 보안 아키텍처

```
[Client] → [API Gateway:8080] → [Microservices]
              │
              ├── JWT 토큰 검증 (JwtUtil)
              ├── 사용자 ID 추출
              └── X-User-Id 헤더 주입
```

**인증 불필요 (Open Endpoints):**
- `POST /api/auth/login` - 로그인
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/refresh` - 토큰 갱신
- `GET /api/concerts/**` - 공연 조회 (읽기 전용)

**인증 필요:**
- `/api/reservations/**` - 모든 예약 API
- `/api/payments/**` - 모든 결제 API

### 서비스 간 통신
- **동기 통신**: OpenFeign (서비스 간 직접 호출) ✅ 구현 완료
- **비동기 통신**: Kafka (이벤트 기반 처리) - 예정

#### OpenFeign 클라이언트 구조
```
reservation-service/
├── client/
│   ├── ConcertClient.java           # Feign 클라이언트 인터페이스
│   ├── ConcertClientFallback.java   # Fallback 처리
│   └── dto/
│       ├── ConcertDto.java          # 공연 정보 DTO
│       ├── VenueDto.java            # 공연장 정보 DTO
│       └── ApiResponseWrapper.java  # 응답 래퍼
```

#### Feign Client 설정
```yaml
# application.yml
feign:
  client:
    config:
      default:
        connect-timeout: 5000
        read-timeout: 5000
      concert-service:
        connect-timeout: 3000
        read-timeout: 3000
```

#### 예약 시 공연 검증 로직
```java
// ReservationServiceImpl.java
private void validateConcertBookingAvailability(UUID concertId) {
    ApiResponseWrapper<ConcertDto> response = concertClient.getConcertById(concertId);

    if (!response.isSuccess()) {
        log.warn("Failed to validate concert from concert-service");
        return; // Fallback: 로컬 검증만 수행
    }

    ConcertDto concert = response.getData();
    if (!concert.getBookingOpen()) {
        throw new IllegalStateException("Booking is not open");
    }
}
```

---

## 백엔드 상세 구조

### Reservation Service (예약 서비스)
```
back/reservation-service/
├── src/main/java/com/tickety/reservation/
│   ├── client/                     # Feign 클라이언트 ✅
│   │   ├── ConcertClient           # Concert Service 호출
│   │   ├── ConcertClientFallback   # Fallback 처리
│   │   └── dto/                    # 클라이언트 DTO
│   ├── config/                     # 설정 (Swagger, JPA)
│   ├── controller/                 # REST API 컨트롤러
│   │   ├── ReservationController   # 예약 API
│   │   └── SeatController          # 좌석 API
│   ├── service/                    # 비즈니스 로직
│   │   └── impl/                   # 서비스 구현체
│   ├── repository/                 # JPA 리포지토리
│   ├── domain/
│   │   ├── entity/                 # Seat, Reservation
│   │   └── enums/                  # 상태 Enum
│   ├── dto/
│   │   ├── request/                # 요청 DTO
│   │   └── response/               # 응답 DTO
│   └── exception/                  # 예외 처리
└── src/test/                       # 테스트 코드
    └── service/
        ├── ReservationServiceTest      # 단위 테스트
        └── ReservationConcurrencyTest  # 동시성 테스트
```

### 핵심 기능: 비관적 락 (Pessimistic Lock)
```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT s FROM Seat s WHERE s.id IN :ids")
List<Seat> findAllByIdWithPessimisticLock(@Param("ids") List<UUID> ids);
```
- 100명의 사용자가 동시에 같은 좌석 예약 시 **1명만 성공**
- `SELECT FOR UPDATE` 쿼리로 데이터 정합성 보장

### 만료된 예약 자동 처리
```java
@Scheduled(fixedRate = 60000)
public void processExpiredReservations() {
    // PENDING 상태이며 expiresAt이 지난 예약 → EXPIRED로 변경
    // 예약된 좌석 → AVAILABLE로 해제
}
```

---

## 프론트엔드 상세 구조

### Feature-Based Architecture
```
front/tickety-web/src/
├── app/                            # 앱 설정
│   ├── App.tsx                     # 메인 앱
│   ├── router.tsx                  # 라우터 설정 (ProtectedRoute 포함)
│   ├── layouts/                    # 레이아웃 (Main, Auth)
│   └── providers/                  # Provider (Query)
│
├── components/
│   ├── common/                     # 공통 컴포넌트
│   │   ├── Button/
│   │   ├── Input/
│   │   ├── Modal/
│   │   ├── Loading/
│   │   └── ProtectedRoute/         # 인증 보호 라우트 ✅
│   └── layout/                     # 레이아웃 컴포넌트
│       ├── Header/
│       └── Footer/
│
├── features/                       # 기능별 모듈
│   ├── auth/                       # 인증 기능
│   │   ├── api/                    # API 호출
│   │   └── hooks/                  # Custom Hooks
│   ├── concert/                    # 공연 기능 ✅
│   │   ├── api/                    # Concert API
│   │   └── hooks/                  # useConcert Hooks
│   └── reservation/                # 예약 기능
│       ├── api/
│       └── hooks/
│
├── pages/                          # 페이지 컴포넌트
│   ├── HomePage.tsx
│   ├── ConcertListPage.tsx         # API 연동 + 샘플 데이터
│   ├── ConcertDetailPage.tsx       # API 연동 + 샘플 데이터
│   ├── ReservationPage.tsx
│   ├── MyReservationsPage.tsx
│   ├── MyPage.tsx
│   └── auth/
│       ├── LoginPage.tsx
│       └── SignupPage.tsx
│
├── services/apiClient.ts           # Axios 인스턴스
├── store/authStore.ts              # Zustand 상태
└── types/index.ts                  # 타입 정의 (UUID 기반)
```

### 인증 보호 라우트
```typescript
// 로그인이 필요한 페이지는 ProtectedRoute로 보호
<Route path="/reservations" element={
  <ProtectedRoute><MyReservationsPage /></ProtectedRoute>
} />
```

**보호된 라우트:**
- `/reservations` - 내 예약 목록
- `/mypage` - 마이페이지
- `/concerts/:id/reserve` - 좌석 예약 페이지

---

## API 명세

### Reservation Service API

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| POST | `/api/reservations` | 예약 생성 | ✅ |
| GET | `/api/reservations` | 내 예약 목록 | ✅ |
| GET | `/api/reservations/{id}` | 예약 상세 조회 | ✅ |
| DELETE | `/api/reservations/{id}` | 예약 취소 | ✅ |
| POST | `/api/reservations/{id}/confirm` | 예약 확정 | ✅ |
| GET | `/api/reservations/seats/concert/{id}` | 공연별 좌석 조회 | ✅ |
| GET | `/api/reservations/seats/concert/{id}/available` | 예약 가능 좌석 | ✅ |
| POST | `/api/reservations/seats/{id}/hold` | 좌석 임시 점유 | ✅ |
| DELETE | `/api/reservations/seats/{id}/hold` | 좌석 점유 해제 | ✅ |

### Auth Service API

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| POST | `/api/auth/signup` | 회원가입 | ❌ |
| POST | `/api/auth/login` | 로그인 | ❌ |
| POST | `/api/auth/refresh` | 토큰 갱신 | ❌ |
| POST | `/api/auth/logout` | 로그아웃 | ✅ |
| GET | `/api/auth/me` | 현재 사용자 정보 | ✅ |

### Concert Service API

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/api/concerts` | 공연 목록 조회 | ❌ |
| GET | `/api/concerts/{id}` | 공연 상세 조회 | ❌ |
| GET | `/api/concerts/upcoming` | 다가오는 공연 조회 | ❌ |
| GET | `/api/concerts/all` | 전체 공연 목록 (관리자) | ✅ |
| POST | `/api/concerts` | 공연 등록 | ✅ (관리자) |
| PATCH | `/api/concerts/{id}/status` | 공연 상태 변경 | ✅ (관리자) |
| DELETE | `/api/concerts/{id}` | 공연 삭제 | ✅ (관리자) |

### Venue API

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/api/venues` | 공연장 목록 조회 | ❌ |
| GET | `/api/venues/{id}` | 공연장 상세 조회 | ❌ |
| GET | `/api/venues/city/{city}` | 도시별 공연장 조회 | ❌ |
| POST | `/api/venues` | 공연장 등록 | ✅ (관리자) |
| PUT | `/api/venues/{id}` | 공연장 수정 | ✅ (관리자) |
| DELETE | `/api/venues/{id}` | 공연장 삭제 | ✅ (관리자) |

### 응답 형식
```json
{
  "success": true,
  "data": { ... },
  "message": "성공",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

### 인증 헤더
```
Authorization: Bearer <jwt-token>
```
- 유효한 토큰 → API Gateway가 `X-User-Id` 헤더 주입
- 잘못된 토큰 → `401 Unauthorized`

---

## 실행 방법

### 사전 요구사항
- Java 17+
- Node.js 18+
- Gradle 8.x

### 1. 백엔드 실행

```bash
# 1. Eureka Server 실행 (필수 - 먼저 실행)
cd back/eureka-server
./gradlew bootRun

# 2. API Gateway 실행
cd back/api-gateway
./gradlew bootRun

# 3. Auth Service 실행
cd back/auth-service
./gradlew bootRun

# 4. Concert Service 실행
cd back/concert-service
./gradlew bootRun

# 5. Reservation Service 실행
cd back/reservation-service
./gradlew bootRun
```

### 2. 프론트엔드 실행

```bash
cd front/tickety-web
npm install
npm run dev
```

### 3. 접속 URL

| 서비스 | URL |
|--------|-----|
| 프론트엔드 | http://localhost:5173 |
| API Gateway | http://localhost:8080 |
| Eureka Dashboard | http://localhost:8761 |
| Swagger UI (Auth) | http://localhost:8081/swagger-ui.html |
| Swagger UI (Concert) | http://localhost:8082/swagger-ui.html |
| Swagger UI (Reservation) | http://localhost:8083/swagger-ui.html |
| H2 Console (Auth) | http://localhost:8081/h2-console |
| H2 Console (Concert) | http://localhost:8082/h2-console |
| H2 Console (Reservation) | http://localhost:8083/h2-console |

---

## 테스트

### 단위 테스트
```bash
cd back/reservation-service
./gradlew test
```

### 동시성 테스트
100명의 사용자가 동시에 같은 좌석을 예약하는 테스트:
```bash
./gradlew test --tests "*ConcurrencyTest*"
```

**예상 결과**: 1명 성공, 99명 실패 (데이터 정합성 보장)

---

## 환경 변수 설정

### 백엔드 (.env)
```bash
# back/.env

# Supabase PostgreSQL (Pooler 사용 권장)
SUPABASE_POOLER_HOST=aws-0-ap-northeast-2.pooler.supabase.com
SUPABASE_POOLER_PORT=5432
SUPABASE_DB=postgres
SUPABASE_USER=postgres.your-project-ref
SUPABASE_PASSWORD=your-database-password

# JWT 설정
JWT_SECRET=your-jwt-secret-key-at-least-32-characters
JWT_EXPIRATION=3600000
```

> ⚠️ `.env` 파일은 `.gitignore`에 포함되어 있습니다. 민감한 정보를 커밋하지 마세요.

### 프론트엔드 (.env)
```bash
# front/tickety-web/.env
VITE_API_BASE_URL=http://localhost:8080
```

---

## 데이터베이스 설계

> **참고:** 모든 ID는 UUID 타입을 사용합니다 (Supabase PostgreSQL)

### 주요 엔티티

| 엔티티 | 설명 | 주요 필드 |
|--------|------|----------|
| `Venue` | 공연장 | name, address, city, totalSeats |
| `Concert` | 공연 | title, artist, concertDate, status, priceMin/Max |
| `Seat` | 좌석 | section, rowNumber, seatNumber, grade, price, status |
| `Reservation` | 예약 | reservationNumber, status, totalAmount, expiresAt |
| `ReservationSeat` | 예약-좌석 매핑 | price (예약 시점 가격) |
| `Payment` | 결제 | paymentNumber, amount, method, status |

### 좌석 (Seat) 테이블
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | UUID | PK |
| concert_id | UUID | 공연 ID (FK) |
| section | String | 구역 (A, B, C) |
| row_number | String | 열 번호 |
| seat_number | Integer | 좌석 번호 |
| grade | Enum | VIP, R, S, A, B |
| price | BigDecimal | 가격 |
| status | Enum | AVAILABLE, HELD, RESERVED, SOLD |

### 예약 (Reservation) 테이블
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | UUID | PK |
| user_id | UUID | 사용자 ID |
| reservation_number | String | 예약 번호 |
| status | Enum | PENDING, CONFIRMED, CANCELLED, EXPIRED |
| total_amount | BigDecimal | 총 금액 |
| created_at | DateTime | 생성 시간 |
| expires_at | DateTime | 만료 시간 (10분) |

### 상태 값 (Enum)

**SeatStatus:**
- `AVAILABLE` - 예약 가능
- `HELD` - 임시 점유 (5분)
- `RESERVED` - 예약됨
- `SOLD` - 판매 완료

**ReservationStatus:**
- `PENDING` - 결제 대기
- `CONFIRMED` - 예약 확정
- `CANCELLED` - 취소됨
- `EXPIRED` - 만료됨

**ConcertStatus:**
- `SCHEDULED` - 예정
- `OPEN` - 예매 중
- `SOLD_OUT` - 매진
- `CANCELLED` - 취소
- `COMPLETED` - 종료

---

## 향후 개발 계획

### Phase 1 (완료) ✅
- [x] 프로젝트 구조 설계
- [x] Eureka Server 구현
- [x] API Gateway 구현
- [x] API Gateway JWT 인증 필터
- [x] Reservation Service 구현
- [x] 동시성 테스트 코드 (비관적 락)
- [x] 프론트엔드 구현
- [x] 프론트엔드 타입 수정 (UUID 기반)
- [x] Concert API 연동 + 샘플 데이터
- [x] 인증 보호 라우트 (ProtectedRoute)
- [x] Supabase PostgreSQL 연동
- [x] Auth Service (JWT 발급)
- [x] Concert Service (공연 관리)

### Phase 2 (완료) ✅
- [x] 서비스 간 통신 (Feign Client)
- [x] 프론트엔드-백엔드 API 연동

### Phase 3 (예정)
- [ ] Payment Service (Saga 패턴)
- [ ] Kafka 이벤트 처리
- [ ] Redis 캐싱
- [ ] Docker Compose 배포

---

## 기여 가이드

1. Feature 브랜치 생성: `feature/기능명`
2. 커밋 메시지 규칙: `feat: 기능 추가`, `fix: 버그 수정`
3. PR 생성 후 코드 리뷰

---

## 라이선스

이 프로젝트는 학습 목적으로 제작되었습니다.
