package com.flyingcaio.flight.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateFlightRequest(
        @NotBlank
        @Size(max = 20)
        String flightNumber,

        @NotBlank
        @Size(min = 3, max = 3)
        String origin,

        @NotBlank
        @Size(min = 3, max = 3)
        String destination,

        @NotNull
        LocalDateTime departureTime,

        @NotNull
        LocalDateTime arrivalTime
) {
}
