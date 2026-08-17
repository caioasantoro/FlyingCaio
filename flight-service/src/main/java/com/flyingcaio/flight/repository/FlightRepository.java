package com.flyingcaio.flight.repository;

import com.flyingcaio.flight.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FlightRepository extends JpaRepository<Flight, UUID> {
}
