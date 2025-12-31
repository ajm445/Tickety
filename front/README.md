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
│   ├── common/             # Button, Input, Modal, Loading
│   └── layout/             # Header, Footer
├── features/               # 기능별 모듈
│   ├── auth/               # 인증
│   ├── concert/            # 공연
│   ├── reservation/        # 예약
│   └── payment/            # 결제
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

| 경로 | 페이지 | 설명 |
|------|--------|------|
| `/` | 홈 | 서비스 소개 |
| `/concerts` | 공연 목록 | 공연 검색 및 목록 |
| `/concerts/:id` | 공연 상세 | 공연 정보 및 예약 버튼 |
| `/concerts/:id/reserve` | 좌석 선택 | 좌석 선택 및 예약 |
| `/reservations` | 내 예약 | 예약 내역 조회 |
| `/auth/login` | 로그인 | 로그인 폼 |
| `/auth/signup` | 회원가입 | 회원가입 폼 |
