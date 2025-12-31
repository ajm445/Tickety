package com.tickety.reservation.exception;

public class SeatNotFoundException extends RuntimeException {

    public SeatNotFoundException(String message) {
        super(message);
    }

    public SeatNotFoundException(Long seatId) {
        super("Seat not found with ID: " + seatId);
    }
}
