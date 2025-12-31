import apiClient from '../../../services/apiClient';
import type { ApiResponse, Reservation, ReservationRequest, Seat } from '../../../types';

export const reservationApi = {
  // Seat APIs
  getSeatsByConcertId: async (concertId: string): Promise<ApiResponse<Seat[]>> => {
    const response = await apiClient.get(`/api/reservations/seats/concert/${concertId}`);
    return response.data;
  },

  getAvailableSeatsByConcertId: async (concertId: string): Promise<ApiResponse<Seat[]>> => {
    const response = await apiClient.get(`/api/reservations/seats/concert/${concertId}/available`);
    return response.data;
  },

  getSeatById: async (seatId: string): Promise<ApiResponse<Seat>> => {
    const response = await apiClient.get(`/api/reservations/seats/${seatId}`);
    return response.data;
  },

  holdSeat: async (seatId: string, holdMinutes: number = 5): Promise<ApiResponse<null>> => {
    const response = await apiClient.post(`/api/reservations/seats/${seatId}/hold?holdMinutes=${holdMinutes}`);
    return response.data;
  },

  releaseSeat: async (seatId: string): Promise<ApiResponse<null>> => {
    const response = await apiClient.delete(`/api/reservations/seats/${seatId}/hold`);
    return response.data;
  },

  // Reservation APIs
  createReservation: async (data: ReservationRequest): Promise<ApiResponse<Reservation>> => {
    const response = await apiClient.post('/api/reservations', data);
    return response.data;
  },

  getMyReservations: async (): Promise<ApiResponse<Reservation[]>> => {
    const response = await apiClient.get('/api/reservations');
    return response.data;
  },

  getReservationById: async (reservationId: string): Promise<ApiResponse<Reservation>> => {
    const response = await apiClient.get(`/api/reservations/${reservationId}`);
    return response.data;
  },

  getReservationByNumber: async (reservationNumber: string): Promise<ApiResponse<Reservation>> => {
    const response = await apiClient.get(`/api/reservations/number/${reservationNumber}`);
    return response.data;
  },

  cancelReservation: async (reservationId: string): Promise<ApiResponse<null>> => {
    const response = await apiClient.delete(`/api/reservations/${reservationId}`);
    return response.data;
  },

  confirmReservation: async (reservationId: string): Promise<ApiResponse<Reservation>> => {
    const response = await apiClient.post(`/api/reservations/${reservationId}/confirm`);
    return response.data;
  },
};
