# 기술 아키텍처 명세서

## 1. 프론트엔드
- **React (Vite), TypeScript, TanStack Query**

## 2. 백엔드 (MSA Infrastructure)
- **Framework:** Spring Boot 3.3.x
- **Gateway:** Spring Cloud Gateway (모든 요청의 진입점)
- **Discovery:** Spring Cloud Netflix Eureka
- **Communication:**
  - 동기: OpenFeign (서비스 간 직접 호출)
  - 비동기: Kafka (예약 성공 -> 결제 요청 등의 이벤트 처리)
- **API Doc:** Springdoc OpenAPI 3.0 (Swagger v3)

## 3. 데이터베이스 (Supabase / PostgreSQL)
- **Database:** Supabase Managed PostgreSQL
- **Strategy:** Service per Database (또는 Schema 분리)
- **Features:** - Row Level Security (RLS) 활용 가능성 검토
  - 복잡한 쿼리를 위한 Querydsl 연동