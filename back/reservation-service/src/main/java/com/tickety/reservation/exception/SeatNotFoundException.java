package com.tickety.reservation.exception;

import java.util.UUID;

public class SeatNotFoundException extends RuntimeException {

    public SeatNotFoundException(String message) {
        super(message);
    }

    public SeatNotFoundException(UUID seatId) {
        super("Seat not found with ID: " + seatId);
    }
}
