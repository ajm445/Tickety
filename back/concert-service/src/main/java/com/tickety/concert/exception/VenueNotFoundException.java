package com.tickety.concert.exception;

import java.util.UUID;

public class VenueNotFoundException extends RuntimeException {

    public VenueNotFoundException(UUID venueId) {
        super("Venue not found with ID: " + venueId);
    }
}
