package com.tickety.reservation.exception;

public class SeatAlreadyReservedException extends RuntimeException {

    public SeatAlreadyReservedException(String message) {
        super(message);
    }

    public SeatAlreadyReservedException(Long seatId) {
        super("Seat with ID " + seatId + " is already reserved.");
    }
}
