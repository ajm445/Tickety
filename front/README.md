# Tickety 웹 - 프론트엔드

React + TypeScript + Vite 기반의 티켓팅 플랫폼 프론트엔드

## 기술 스택

- **프레임워크**: React 19 + Vite
- **언어**: TypeScript
- **스타일링**: Tailwind CSS
- **상태 관리**: TanStack Query + Zustand
- **라우팅**: React Router v7
- **폼 관리**: React Hook Form + Zod
- **아이콘**: Lucide React

## 프로젝트 구조

```
tickety-web/src/
├── app/                    # 앱 설정
│   ├── App.tsx
│   ├── router.tsx
│   ├── layouts/
│   └── providers/
├── components/             # 공통 UI 컴포넌트
│   ├── common/             # Button, Input, Modal, Loading, ProtectedRoute
│   └── layout/             # Header, Footer
├── features/               # 기능별 모듈
│   ├── auth/               # 인증 (로그인, 회원가입)
│   ├── concert/            # 공연 (목록, 상세)
│   └── reservation/        # 예약 (좌석 선택, 예약 관리)
├── pages/                  # 페이지 컴포넌트
├── services/               # API 클라이언트
├── store/                  # 전역 상태 (Zustand)
├── types/                  # 타입 정의
└── utils/                  # 유틸리티 함수
```

## 시작하기

```bash
cd tickety-web
npm install
npm run dev
```

브라우저에서 http://localhost:5173 접속

## 환경 변수 설정

```bash
cp .env.example .env
```

| 변수 | 설명 | 기본값 |
|----------|-------------|---------|
| VITE_API_BASE_URL | 백엔드 API URL | http://localhost:8080 |

## 주요 페이지

| 경로 | 페이지 | 설명 | 인증 필요 |
|------|--------|------|----------|
| `/` | 홈 | 서비스 소개 | X |
| `/concerts` | 공연 목록 | 공연 검색 및 목록 | X |
| `/concerts/:id` | 공연 상세 | 공연 정보 및 예약 버튼 | X |
| `/concerts/:id/reserve` | 좌석 선택 | 좌석 선택 및 예약 | O |
| `/reservations` | 내 예약 | 예약 내역 조회 및 취소 | O |
| `/mypage` | 마이페이지 | 회원 정보 관리 | O |
| `/auth/login` | 로그인 | 로그인 폼 | X |
| `/auth/signup` | 회원가입 | 회원가입 폼 | X |

## 구현 상태

### 완료된 기능
- [x] 사용자 인증 (로그인, 회원가입, 로그아웃)
- [x] 공연 목록 조회 (API 연동 + 샘플 데이터 fallback)
- [x] 공연 상세 조회 (API 연동 + 샘플 데이터 fallback)
- [x] 좌석 선택 및 예약
- [x] 내 예약 조회 및 취소
- [x] 인증 보호 라우트 (ProtectedRoute)
- [x] 반응형 UI
- [x] 토큰 자동 갱신

### 미구현 기능
- [ ] 결제 페이지 (백엔드 API 미연결)
- [ ] 마이페이지 프로필 수정
- [ ] 비밀번호 변경

## API 연동 상태

| 기능 | API 경로 | 상태 |
|------|---------|------|
| 로그인 | POST /api/auth/login | 연동 완료 |
| 회원가입 | POST /api/auth/signup | 연동 완료 |
| 토큰 갱신 | POST /api/auth/refresh | 연동 완료 |
| 공연 목록 | GET /api/concerts | 연동 완료 |
| 공연 상세 | GET /api/concerts/:id | 연동 완료 |
| 좌석 조회 | GET /api/reservations/seats/concert/:id | 연동 완료 |
| 예약 생성 | POST /api/reservations | 연동 완료 |
| 예약 조회 | GET /api/reservations | 연동 완료 |
| 예약 취소 | DELETE /api/reservations/:id | 연동 완료 |
| 결제 처리 | - | 미연동 |

## 타입 정의

모든 API 응답 타입은 `src/types/index.ts`에 정의되어 있으며, Supabase DB 스키마와 일치하도록 설계되었습니다.

- `Concert`, `Venue`, `Seat`, `Reservation`, `Payment` 등 주요 엔티티 타입
- `ApiResponse<T>` 공통 응답 wrapper
- `database.types.ts` - Supabase 자동 생성 타입

## 개발 가이드

### 새 기능 추가

1. `features/` 폴더에 기능별 모듈 생성
2. `api/` - API 호출 함수
3. `hooks/` - React Query 훅
4. `index.ts` - export 관리

### 컴포넌트 규칙

- 공통 UI 컴포넌트는 `components/common/`에 생성
- 각 컴포넌트는 폴더로 관리 (Component/, index.ts)
- Props 타입은 컴포넌트와 함께 export
