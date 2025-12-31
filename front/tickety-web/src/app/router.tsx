import { createBrowserRouter } from 'react-router-dom';
import { MainLayout } from './layouts/MainLayout';
import { AuthLayout } from './layouts/AuthLayout';
import { ProtectedRoute } from '../components/common';

// Pages
import HomePage from '../pages/HomePage';
import ConcertListPage from '../pages/ConcertListPage';
import ConcertDetailPage from '../pages/ConcertDetailPage';
import ReservationPage from '../pages/ReservationPage';
import MyReservationsPage from '../pages/MyReservationsPage';
import MyPage from '../pages/MyPage';
import LoginPage from '../pages/auth/LoginPage';
import SignupPage from '../pages/auth/SignupPage';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <MainLayout />,
    children: [
      {
        index: true,
        element: <HomePage />,
      },
      {
        path: 'concerts',
        element: <ConcertListPage />,
      },
      {
        path: 'concerts/:concertId',
        element: <ConcertDetailPage />,
      },
      {
        path: 'concerts/:concertId/reserve',
        element: (
          <ProtectedRoute>
            <ReservationPage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'reservations',
        element: (
          <ProtectedRoute>
            <MyReservationsPage />
          </ProtectedRoute>
        ),
      },
      {
        path: 'mypage',
        element: (
          <ProtectedRoute>
            <MyPage />
          </ProtectedRoute>
        ),
      },
    ],
  },
  {
    path: '/auth',
    element: <AuthLayout />,
    children: [
      {
        path: 'login',
        element: <LoginPage />,
      },
      {
        path: 'signup',
        element: <SignupPage />,
      },
    ],
  },
]);
