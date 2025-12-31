package com.tickety.reservation.exception;

import java.util.UUID;

public class ConcertNotFoundException extends RuntimeException {

    public ConcertNotFoundException(String message) {
        super(message);
    }

    public ConcertNotFoundException(UUID concertId) {
        super("Concert not found with ID: " + concertId);
    }
}
