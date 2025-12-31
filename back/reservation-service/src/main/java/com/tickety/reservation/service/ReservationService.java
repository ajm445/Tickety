package com.tickety.reservation.service;

import com.tickety.reservation.dto.request.ReservationRequest;
import com.tickety.reservation.dto.response.ReservationResponse;

import java.util.List;

public interface ReservationService {

    List<ReservationResponse> createReservation(ReservationRequest request, Long userId);

    List<ReservationResponse> getReservationsByUserId(Long userId);

    ReservationResponse getReservationById(Long reservationId);

    void cancelReservation(Long reservationId, Long userId);

    ReservationResponse confirmReservation(Long reservationId);

    void processExpiredReservations();
}
