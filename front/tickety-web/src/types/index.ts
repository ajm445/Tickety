// API Response Types
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string;
  timestamp: string;
}

// Auth Types
export interface User {
  userId: number;
  email: string;
  name: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  email: string;
  password: string;
  name: string;
}

export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
}

// Concert Types
export interface Concert {
  id: number;
  title: string;
  description: string;
  venue: Venue;
  startDate: string;
  endDate: string;
  posterUrl?: string;
  category: string;
}

export interface Venue {
  id: number;
  name: string;
  address: string;
  capacity: number;
}

// Seat Types
export type SeatGrade = 'VIP' | 'R' | 'S' | 'A';
export type SeatStatus = 'AVAILABLE' | 'RESERVED' | 'SOLD';

export interface Seat {
  seatId: number;
  concertId: number;
  seatNumber: string;
  seatGrade: SeatGrade;
  price: number;
  status: SeatStatus;
}

// Reservation Types
export type ReservationStatus = 'PENDING' | 'CONFIRMED' | 'CANCELLED';

export interface Reservation {
  reservationId: number;
  seatId: number;
  userId: number;
  status: ReservationStatus;
  reservedAt: string;
  expiredAt: string;
}

export interface ReservationRequest {
  concertId: number;
  seatIds: number[];
}

// Payment Types
export type PaymentStatus = 'PENDING' | 'COMPLETED' | 'FAILED' | 'REFUNDED';
export type PaymentMethod = 'CARD' | 'BANK_TRANSFER' | 'VIRTUAL_ACCOUNT';

export interface Payment {
  paymentId: number;
  reservationId: number;
  amount: number;
  method: PaymentMethod;
  status: PaymentStatus;
  paidAt?: string;
}

export interface PaymentRequest {
  reservationId: number;
  method: PaymentMethod;
}
