# 프로젝트명: Tickety (Enterprise MSA Platform)

## 1. 개요
데이터 정합성이 중요한 티켓팅 도메인을 MSA 구조로 설계하며, 자바 진영의 표준 기술(Spring Boot, JPA)과 Supabase(PostgreSQL)를 활용해 대규모 트래픽 대응 능력을 증명함.

## 2. 핵심 서비스 정의
- **Auth Service:** Supabase Auth 또는 JWT 기반 인증 처리.
- **Concert Service:** 공연/공연장 정보 관리 (Swagger API 필수).
- **Reservation Service:** 좌석 점유 및 예약 로직. 비관적 락(Pessimistic Lock) 적용.
- **Payment Service:** 결제 처리 및 Saga 패턴을 통한 트랜잭션 복구.

## 3. 테스트 및 품질 전략 (클로드 코드 지시용)
- **Unit Test:** JUnit 5와 Mockito를 사용하여 서비스 로직의 90% 이상 커버리지 확보.
- **Integration Test:** `@SpringBootTest`를 활용하여 DB 연동 테스트 수행.
- **API Documentation:** `springdoc-openapi-ui`를 활용하여 모든 API의 Swagger 문서를 자동 생성.