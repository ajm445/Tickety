# Tickety - 티켓팅 플랫폼

MSA(Microservice Architecture) 기반의 콘서트 티켓 예약 플랫폼

## 프로젝트 개요

Tickety는 대규모 트래픽 환경에서 데이터 정합성을 보장하는 티켓팅 시스템입니다. 수만 명의 사용자가 동시에 같은 좌석을 예약하려 할 때 발생하는 동시성 문제를 비관적 락(Pessimistic Lock)으로 해결합니다.

### 핵심 기능
- 공연 정보 조회 및 좌석 선택
- 실시간 좌석 예약 (동시성 제어)
- 결제 처리 (Saga 패턴)
- JWT 기반 사용자 인증

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
    ├── auth-service/               # 인증 서비스 (예정)
    ├── concert-service/            # 공연 서비스 (예정)
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
| Eureka Server | 8761 | 서비스 디스커버리 | ✅ 구현 완료 |
| API Gateway | 8080 | 라우팅, CORS, 인증 필터 | ✅ 구현 완료 |
| Auth Service | 8081 | JWT 인증/인가 | 📋 예정 |
| Concert Service | 8082 | 공연/공연장 관리 | 📋 예정 |
| Reservation Service | 8083 | 좌석 예약 (비관적 락) | ✅ 구현 완료 |
| Payment Service | 8084 | 결제 처리 (Saga) | 📋 예정 |
| Frontend | 5173 | React 웹 애플리케이션 | ✅ 구현 완료 |

### 서비스 간 통신
- **동기 통신**: OpenFeign (서비스 간 직접 호출)
- **비동기 통신**: Kafka (이벤트 기반 처리) - 예정

---

## 백엔드 상세 구조

### Reservation Service (예약 서비스)
```
back/reservation-service/
├── src/main/java/com/tickety/reservation/
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
@Query("SELECT s FROM Seat s WHERE s.id = :id")
Optional<Seat> findByIdWithPessimisticLock(@Param("id") Long id);
```
- 100명의 사용자가 동시에 같은 좌석 예약 시 **1명만 성공**
- `SELECT FOR UPDATE` 쿼리로 데이터 정합성 보장

---

## 프론트엔드 상세 구조

### Feature-Based Architecture
```
front/tickety-web/src/
├── app/                            # 앱 설정
│   ├── App.tsx                     # 메인 앱
│   ├── router.tsx                  # 라우터 설정
│   ├── layouts/                    # 레이아웃 (Main, Auth)
│   └── providers/                  # Provider (Query)
│
├── components/
│   ├── common/                     # 공통 컴포넌트
│   │   ├── Button/
│   │   ├── Input/
│   │   ├── Modal/
│   │   └── Loading/
│   └── layout/                     # 레이아웃 컴포넌트
│       ├── Header/
│       └── Footer/
│
├── features/                       # 기능별 모듈
│   ├── auth/                       # 인증 기능
│   │   ├── api/                    # API 호출
│   │   └── hooks/                  # Custom Hooks
│   └── reservation/                # 예약 기능
│       ├── api/
│       └── hooks/
│
├── pages/                          # 페이지 컴포넌트
│   ├── HomePage.tsx
│   ├── ConcertListPage.tsx
│   ├── ReservationPage.tsx
│   ├── MyReservationsPage.tsx
│   ├── MyPage.tsx
│   └── auth/
│       ├── LoginPage.tsx
│       └── SignupPage.tsx
│
├── services/apiClient.ts           # Axios 인스턴스
├── store/authStore.ts              # Zustand 상태
└── types/index.ts                  # 타입 정의
```

---

## API 명세

### Reservation Service API

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/reservations` | 예약 생성 |
| GET | `/api/reservations` | 내 예약 목록 |
| GET | `/api/reservations/{id}` | 예약 상세 조회 |
| DELETE | `/api/reservations/{id}` | 예약 취소 |
| POST | `/api/reservations/{id}/confirm` | 예약 확정 |
| GET | `/api/reservations/seats/concert/{id}` | 공연별 좌석 조회 |
| GET | `/api/reservations/seats/concert/{id}/available` | 예약 가능 좌석 |

### 응답 형식
```json
{
  "success": true,
  "data": { ... },
  "message": "성공",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

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

# 3. Reservation Service 실행
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
| Swagger UI (Reservation) | http://localhost:8083/swagger-ui.html |
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
SUPABASE_HOST=your-project-ref.supabase.co
SUPABASE_PORT=5432
SUPABASE_DB=postgres
SUPABASE_USER=postgres
SUPABASE_PASSWORD=your-password
```

### 프론트엔드 (.env)
```bash
# front/tickety-web/.env
VITE_API_BASE_URL=http://localhost:8080
```

---

## 데이터베이스 설계

### Seat (좌석) 테이블
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| concert_id | Long | 공연 ID |
| seat_number | String | 좌석 번호 (A-1) |
| seat_grade | Enum | VIP, R, S, A |
| price | BigDecimal | 가격 |
| status | Enum | AVAILABLE, RESERVED, SOLD |

### Reservation (예약) 테이블
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| seat_id | Long | 좌석 ID |
| user_id | Long | 사용자 ID |
| status | Enum | PENDING, CONFIRMED, CANCELLED |
| reserved_at | DateTime | 예약 시간 |
| expired_at | DateTime | 만료 시간 (10분) |

---

## 향후 개발 계획

### Phase 1 (완료)
- [x] 프로젝트 구조 설계
- [x] Eureka Server 구현
- [x] API Gateway 구현
- [x] Reservation Service 구현
- [x] 동시성 테스트 코드
- [x] 프론트엔드 구현

### Phase 2 (예정)
- [ ] Auth Service (JWT 인증)
- [ ] Concert Service (공연 관리)
- [ ] Supabase 연동

### Phase 3 (예정)
- [ ] Payment Service (Saga 패턴)
- [ ] Kafka 이벤트 처리
- [ ] Docker Compose 배포

---

## 기여 가이드

1. Feature 브랜치 생성: `feature/기능명`
2. 커밋 메시지 규칙: `feat: 기능 추가`, `fix: 버그 수정`
3. PR 생성 후 코드 리뷰

---

## 라이선스

이 프로젝트는 학습 목적으로 제작되었습니다.
