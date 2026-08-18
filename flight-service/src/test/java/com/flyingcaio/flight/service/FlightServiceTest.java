package com.flyingcaio.flight.service;

import com.flyingcaio.flight.domain.Flight;
import com.flyingcaio.flight.domain.FlightStatus;
import com.flyingcaio.flight.domain.exception.FlightNotFoundException;
import com.flyingcaio.flight.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void shouldCreateFlight() {
        LocalDateTime departure =
                LocalDateTime.of(2026, 9, 20, 10, 0);

        LocalDateTime arrival =
                LocalDateTime.of(2026, 9, 20, 12, 0);

        when(flightRepository.save(any(Flight.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Flight result = flightService.create(
                "fc1001",
                "lis",
                "mad",
                departure,
                arrival
        );

        assertThat(result.getFlightNumber()).isEqualTo("FC1001");
        assertThat(result.getOrigin()).isEqualTo("LIS");
        assertThat(result.getDestination()).isEqualTo("MAD");
        assertThat(result.getStatus()).isEqualTo(FlightStatus.SCHEDULED);

        verify(flightRepository, times(1))
                .save(any(Flight.class));
    }

    @Test
    void shouldPersistCorrectFlightData() {
        LocalDateTime departure =
                LocalDateTime.of(2026, 9, 20, 10, 0);

        LocalDateTime arrival =
                LocalDateTime.of(2026, 9, 20, 12, 0);

        when(flightRepository.save(any(Flight.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        flightService.create(
                "fc2001",
                "lis",
                "cdg",
                departure,
                arrival
        );

        ArgumentCaptor<Flight> captor =
                ArgumentCaptor.forClass(Flight.class);

        verify(flightRepository).save(captor.capture());

        Flight capturedFlight = captor.getValue();

        assertThat(capturedFlight.getFlightNumber()).isEqualTo("FC2001");
        assertThat(capturedFlight.getOrigin()).isEqualTo("LIS");
        assertThat(capturedFlight.getDestination()).isEqualTo("CDG");
        assertThat(capturedFlight.getDepartureTime()).isEqualTo(departure);
        assertThat(capturedFlight.getArrivalTime()).isEqualTo(arrival);
    }

    @Test
    void shouldFindFlightById() {
        UUID id = UUID.randomUUID();

        Flight flight = Flight.create(
                "FC3001",
                "LIS",
                "MAD",
                LocalDateTime.of(2026, 9, 20, 10, 0),
                LocalDateTime.of(2026, 9, 20, 12, 0)
        );

        when(flightRepository.findById(id))
                .thenReturn(Optional.of(flight));

        Flight result = flightService.findById(id);

        assertThat(result).isSameAs(flight);

        verify(flightRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenFlightDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(flightRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.findById(id))
                .isInstanceOf(FlightNotFoundException.class)
                .hasMessage("Flight not found: " + id);

        verify(flightRepository).findById(id);
    }
}