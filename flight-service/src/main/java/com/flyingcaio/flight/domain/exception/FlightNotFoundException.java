package com.flyingcaio.flight.domain.exception;

import java.util.UUID;

public class FlightNotFoundException extends RuntimeException {

    public FlightNotFoundException(UUID id) {
        super("Flight not found: " + id);
    }
}