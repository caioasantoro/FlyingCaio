package com.flyingcaio.flight.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FlightTest {

    @Test
    void shouldCreateScheduledFlight() {
        LocalDateTime departure = LocalDateTime.of(2026, 9, 20, 10, 0);
        LocalDateTime arrival = LocalDateTime.of(2026, 9, 20, 12, 0);

        Flight flight = Flight.create(
                "fc1001",
                "lis",
                "mad",
                departure,
                arrival
        );

        assertThat(flight.getFlightNumber()).isEqualTo("FC1001");
        assertThat(flight.getOrigin()).isEqualTo("LIS");
        assertThat(flight.getDestination()).isEqualTo("MAD");
        assertThat(flight.getStatus()).isEqualTo(FlightStatus.SCHEDULED);
        assertThat(flight.getCreatedAt()).isNotNull();
        assertThat(flight.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldRejectFlightWithSameOriginAndDestination() {
        LocalDateTime departure = LocalDateTime.of(2026, 9, 20, 10, 0);
        LocalDateTime arrival = LocalDateTime.of(2026, 9, 20, 12, 0);

        assertThatThrownBy(() ->
                Flight.create(
                        "FC1001",
                        "LIS",
                        "LIS",
                        departure,
                        arrival
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Origin and destination must be different");
    }

    @Test
    void shouldRejectFlightWhenArrivalIsBeforeDeparture() {
        LocalDateTime departure = LocalDateTime.of(2026, 9, 20, 12, 0);
        LocalDateTime arrival = LocalDateTime.of(2026, 9, 20, 10, 0);

        assertThatThrownBy(() ->
                Flight.create(
                        "FC1001",
                        "LIS",
                        "MAD",
                        departure,
                        arrival
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Arrival time must be after departure time");
    }

    @Test
    void shouldRejectNullFlightNumber() {
        assertThatThrownBy(() ->
                Flight.create(
                        null,
                        "LIS",
                        "MAD",
                        LocalDateTime.of(2026, 9, 20, 10, 0),
                        LocalDateTime.of(2026, 9, 20, 12, 0)
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Flight number is required");
    }
}