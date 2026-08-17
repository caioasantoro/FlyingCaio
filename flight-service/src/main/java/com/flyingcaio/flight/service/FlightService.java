package com.flyingcaio.flight.service;

import com.flyingcaio.flight.domain.Flight;
import com.flyingcaio.flight.domain.exception.FlightNotFoundException;
import com.flyingcaio.flight.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository){
        this.flightRepository=flightRepository;
    }

    public Flight create(
            String flightNumber,
            String origin,
            String destination,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime) {

        Flight flight = Flight.create(
                flightNumber,
                origin,
                destination,
                departureTime,
                arrivalTime
        );

        return flightRepository.save(flight);
    }

    public Flight findById(UUID id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(id));
    }

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }
}
