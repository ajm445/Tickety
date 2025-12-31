package com.tickety.reservation.service;

import com.tickety.reservation.dto.request.ReservationRequest;
import com.tickety.reservation.dto.response.ReservationResponse;

import java.util.List;
import java.util.UUID;

public interface ReservationService {

    ReservationResponse createReservation(ReservationRequest request, UUID userId);

    List<ReservationResponse> getReservationsByUserId(UUID userId);

    ReservationResponse getReservationById(UUID reservationId);

    ReservationResponse getReservationByNumber(String reservationNumber);

    void cancelReservation(UUID reservationId, UUID userId);

    ReservationResponse confirmReservation(UUID reservationId);

    void processExpiredReservations();
}
