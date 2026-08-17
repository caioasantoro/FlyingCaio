package com.flyingcaio.flight.controller;

import com.flyingcaio.flight.domain.Flight;
import com.flyingcaio.flight.domain.FlightStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record FlightResponse(
        UUID id,
        String flightNumber,
        String origin,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        FlightStatus status
) {

    public static FlightResponse from(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getStatus()
        );
    }
}