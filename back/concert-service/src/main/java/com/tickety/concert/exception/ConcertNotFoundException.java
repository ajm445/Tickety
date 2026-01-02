package com.tickety.concert.exception;

import java.util.UUID;

public class ConcertNotFoundException extends RuntimeException {

    public ConcertNotFoundException(UUID concertId) {
        super("Concert not found with ID: " + concertId);
    }
}
