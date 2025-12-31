// API Response Types
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string;
  timestamp: string;
}

// Auth Types
export interface User {
  id: string;  // UUID
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

// Venue Types
export interface Venue {
  id: string;  // UUID
  name: string;
  address: string;
  city: string;
  totalSeats: number;
  description?: string;
  imageUrl?: string;
}

// Concert Types
export type ConcertStatus = 'SCHEDULED' | 'OPEN' | 'SOLD_OUT' | 'CANCELLED' | 'COMPLETED';

export interface Concert {
  id: string;  // UUID
  venueId: string;
  venue?: Venue;
  title: string;
  artist: string;
  description?: string;
  concertDate: string;
  bookingStartAt: string;
  bookingEndAt: string;
  status: ConcertStatus;
  posterUrl?: string;
  priceMin: number;
  priceMax: number;
}

// Seat Types
export type SeatGrade = 'VIP' | 'R' | 'S' | 'A' | 'B';
export type SeatStatus = 'AVAILABLE' | 'HELD' | 'RESERVED' | 'SOLD';

export interface Seat {
  id: string;  // UUID
  concertId: string;
  section: string;
  rowNumber: string;
  seatNumber: number;
  fullSeatNumber: string;
  grade: SeatGrade;
  price: number;
  status: SeatStatus;
}

// Reservation Types
export type ReservationStatus = 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'EXPIRED';

export interface ReservationSeat {
  id: string;  // UUID
  seatId: string;
  fullSeatNumber: string;
  grade: SeatGrade;
  price: number;
}

export interface Reservation {
  id: string;  // UUID
  reservationNumber: string;
  userId: string;
  concertId: string;
  concertTitle: string;
  status: ReservationStatus;
  totalAmount: number;
  seats?: ReservationSeat[];
  createdAt: string;
  expiresAt: string;
  confirmedAt?: string;
}

export interface ReservationRequest {
  concertId: string;
  seatIds: string[];
}

// Payment Types
export type PaymentStatus = 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED' | 'REFUNDED' | 'PARTIAL_REFUNDED';
export type PaymentMethod = 'CREDIT_CARD' | 'DEBIT_CARD' | 'BANK_TRANSFER' | 'KAKAO_PAY' | 'NAVER_PAY' | 'TOSS_PAY';

export interface Payment {
  id: string;  // UUID
  reservationId: string;
  userId: string;
  paymentNumber: string;
  amount: number;
  method: PaymentMethod;
  status: PaymentStatus;
  pgTransactionId?: string;
  pgProvider?: string;
  paidAt?: string;
  failedAt?: string;
  failureReason?: string;
  refundedAmount?: number;
  refundedAt?: string;
  refundReason?: string;
}

export interface PaymentRequest {
  reservationId: string;
  method: PaymentMethod;
}
