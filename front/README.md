# Tickety Web - Frontend

React + TypeScript + Vite 기반의 티켓팅 플랫폼 프론트엔드

## Tech Stack

- **Framework**: React 19 + Vite
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **State Management**: TanStack Query + Zustand
- **Routing**: React Router v7
- **Form**: React Hook Form + Zod
- **Icons**: Lucide React

## Project Structure

```
tickety-web/src/
├── app/                    # App configuration
│   ├── App.tsx
│   ├── router.tsx
│   ├── layouts/
│   └── providers/
├── components/             # Shared UI components
│   ├── common/             # Button, Input, Modal, Loading
│   └── layout/             # Header, Footer
├── features/               # Feature-based modules
│   ├── auth/
│   ├── concert/
│   ├── reservation/
│   └── payment/
├── pages/                  # Page components
├── services/               # API client
├── store/                  # Global state (Zustand)
├── types/                  # Type definitions
└── utils/                  # Utility functions
```

## Getting Started

```bash
cd tickety-web
npm install
npm run dev
```

Open http://localhost:5173

## Environment Variables

```bash
cp .env.example .env
```

| Variable | Description | Default |
|----------|-------------|---------|
| VITE_API_BASE_URL | Backend API URL | http://localhost:8080 |
