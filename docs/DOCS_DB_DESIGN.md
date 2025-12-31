# 데이터베이스 설계 전략

## 1. Supabase PostgreSQL 설정
- 각 마이크로서비스는 별도의 Supabase 프로젝트 또는 스키마를 사용함.
- `Reservations` 테이블과 `Seats` 테이블은 관계형 무결성을 유지함.

## 2. 동시성 제어 (Concurrency Control)
- **문제:** 수만 명의 유저가 동일 좌석을 선택할 때 'Double Booking' 방지.
- **해결:** Reservation Service에서 `SELECT FOR UPDATE` (Pessimistic Lock) 사용.
- **테스트 케이스:** `ExecutorService`를 활용한 멀티스레드 환경의 동시성 테스트 필수 포함.

## 3. Swagger 설정 가이드
- `@OpenAPIDefinition`을 통한 서비스별 메타데이터 설정.
- 각 Controller 메서드에 `@Operation`, `@ApiResponse` 어노테이션을 부착하여 상세 문서화.