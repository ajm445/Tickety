# 클로드 코드 작업 가이드

## 작업 1: 프로젝트 초기화 및 MSA 인프라
"루트의 DOCS_ARCHITECTURE.md를 읽고, Spring Cloud Gateway와 Eureka Server 프로젝트를 생성해줘. 모든 프로젝트에는 springdoc-openapi 의존성을 포함시켜줘."

## 작업 2: 엔티티 및 Swagger 연동
"Reservation Service를 구현해줘. DOCS_DB_DESIGN.md에 정의된 대로 Seats, Reservations 엔티티를 만들고, 모든 API에 Swagger 어노테이션을 달아서 자동으로 문서를 생성할 수 있게 해줘."

## 작업 3: 테스트 코드 생성
"작성된 예약 로직(ReservationService.java)에 대해 JUnit 5 테스트 코드를 생성해줘. 특히 100명의 유저가 동시에 같은 좌석을 예약하려 할 때 한 명만 성공하는지 확인하는 멀티스레드 동시성 테스트 코드를 작성해줘."

## 작업 4: Supabase 연동
"application.yml에 Supabase PostgreSQL 연결 정보를 설정하는 템플릿을 만들어줘. DB 커넥션 풀 설정을 최적화해줘."