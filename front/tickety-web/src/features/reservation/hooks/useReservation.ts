import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { reservationApi } from '../api/reservationApi';
import type { ReservationRequest } from '../../../types';

export const useSeats = (concertId: string | undefined) => {
  return useQuery({
    queryKey: ['seats', concertId],
    queryFn: () => reservationApi.getSeatsByConcertId(concertId!),
    select: (response) => response.data,
    enabled: !!concertId,
  });
};

export const useAvailableSeats = (concertId: string | undefined) => {
  return useQuery({
    queryKey: ['seats', concertId, 'available'],
    queryFn: () => reservationApi.getAvailableSeatsByConcertId(concertId!),
    select: (response) => response.data,
    enabled: !!concertId,
  });
};

export const useMyReservations = () => {
  return useQuery({
    queryKey: ['reservations', 'my'],
    queryFn: () => reservationApi.getMyReservations(),
    select: (response) => response.data,
  });
};

export const useReservation = (reservationId: string | undefined) => {
  return useQuery({
    queryKey: ['reservations', reservationId],
    queryFn: () => reservationApi.getReservationById(reservationId!),
    select: (response) => response.data,
    enabled: !!reservationId,
  });
};

export const useCreateReservation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: ReservationRequest) => reservationApi.createReservation(data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ['seats', variables.concertId] });
      queryClient.invalidateQueries({ queryKey: ['reservations', 'my'] });
    },
  });
};

export const useCancelReservation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (reservationId: string) => reservationApi.cancelReservation(reservationId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['reservations'] });
      queryClient.invalidateQueries({ queryKey: ['seats'] });
    },
  });
};

export const useConfirmReservation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (reservationId: string) => reservationApi.confirmReservation(reservationId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['reservations'] });
    },
  });
};
