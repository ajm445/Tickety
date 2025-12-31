package com.tickety.reservation.exception;

import java.util.UUID;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(UUID reservationId) {
        super("Reservation not found with ID: " + reservationId);
    }
}
