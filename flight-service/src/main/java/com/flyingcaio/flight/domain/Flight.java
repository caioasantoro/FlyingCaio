package com.flyingcaio.flight.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "flight",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_flight_number_departure",
                        columnNames = {"flight_number", "departure_time"}
                )
        })
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "flight_number", nullable = false, length = 20)
    private String flightNumber;

    @Column(nullable = false, length = 3)
    private String origin;

    @Column(nullable = false, length = 3)
    private String destination;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FlightStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static Flight create(
            String flightNumber,
            String origin,
            String destination,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime) {

        if (flightNumber == null || flightNumber.isBlank()) {
            throw new IllegalArgumentException("Flight number is required");
        }

        if (origin == null || origin.isBlank()) {
            throw new IllegalArgumentException("Origin is required");
        }

        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination is required");
        }

        if (departureTime == null) {
            throw new IllegalArgumentException("Departure time is required");
        }

        if (arrivalTime == null) {
            throw new IllegalArgumentException("Arrival time is required");
        }

        if (origin.equalsIgnoreCase(destination)) {
            throw new IllegalArgumentException(
                    "Origin and destination must be different"
            );
        }

        if (!arrivalTime.isAfter(departureTime)) {
            throw new IllegalArgumentException(
                    "Arrival time must be after departure time"
            );
        }

        Flight flight = new Flight();

        flight.flightNumber = flightNumber.toUpperCase();
        flight.origin = origin.toUpperCase();
        flight.destination = destination.toUpperCase();
        flight.departureTime = departureTime;
        flight.arrivalTime = arrivalTime;
        flight.status = FlightStatus.SCHEDULED;

        LocalDateTime now = LocalDateTime.now();
        flight.createdAt = now;
        flight.updatedAt = now;

        return flight;

}
}
