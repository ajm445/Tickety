import apiClient from '../../../services/apiClient';
import type { ApiResponse, Reservation, ReservationRequest, Seat } from '../../../types';

export const reservationApi = {
  // Seat APIs
  getSeatsByConcertId: async (concertId: number): Promise<ApiResponse<Seat[]>> => {
    const response = await apiClient.get(`/api/reservations/seats/concert/${concertId}`);
    return response.data;
  },

  getAvailableSeatsByConcertId: async (concertId: number): Promise<ApiResponse<Seat[]>> => {
    const response = await apiClient.get(`/api/reservations/seats/concert/${concertId}/available`);
    return response.data;
  },

  getSeatById: async (seatId: number): Promise<ApiResponse<Seat>> => {
    const response = await apiClient.get(`/api/reservations/seats/${seatId}`);
    return response.data;
  },

  // Reservation APIs
  createReservation: async (data: ReservationRequest): Promise<ApiResponse<Reservation[]>> => {
    const response = await apiClient.post('/api/reservations', data);
    return response.data;
  },

  getMyReservations: async (): Promise<ApiResponse<Reservation[]>> => {
    const response = await apiClient.get('/api/reservations');
    return response.data;
  },

  getReservationById: async (reservationId: number): Promise<ApiResponse<Reservation>> => {
    const response = await apiClient.get(`/api/reservations/${reservationId}`);
    return response.data;
  },

  cancelReservation: async (reservationId: number): Promise<ApiResponse<null>> => {
    const response = await apiClient.delete(`/api/reservations/${reservationId}`);
    return response.data;
  },

  confirmReservation: async (reservationId: number): Promise<ApiResponse<Reservation>> => {
    const response = await apiClient.post(`/api/reservations/${reservationId}/confirm`);
    return response.data;
  },
};
