package com.flyingcaio.flight.controller;

import com.flyingcaio.flight.domain.Flight;
import com.flyingcaio.flight.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse create(@Valid @RequestBody CreateFlightRequest request) {

        Flight flight = flightService.create(
                request.flightNumber(),
                request.origin(),
                request.destination(),
                request.departureTime(),
                request.arrivalTime()
        );

        return FlightResponse.from(flight);
    }


    @GetMapping("/{id}")
    public FlightResponse findById(@PathVariable UUID id) {
        return FlightResponse.from(
                flightService.findById(id)
        );
    }

    @GetMapping
    public List<FlightResponse> findAll() {
        return flightService.findAll()
                .stream()
                .map(FlightResponse::from)
                .toList();
    }

}