package com.tickety.reservation.exception;

import java.util.UUID;

public class SeatAlreadyReservedException extends RuntimeException {

    public SeatAlreadyReservedException(String message) {
        super(message);
    }

    public SeatAlreadyReservedException(UUID seatId) {
        super("Seat with ID " + seatId + " is already reserved.");
    }
}
