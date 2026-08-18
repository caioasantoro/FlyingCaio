package com.flyingcaio.flight.repository;

import com.flyingcaio.flight.domain.Flight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@DataJpaTest
class FlightRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("flyingcaio_test")
                    .withUsername("flyingcaio")
                    .withPassword("flyingcaio");

    @DynamicPropertySource
    static void configurePostgres(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private FlightRepository flightRepository;

    @Test
    void shouldPersistAndFindFlight() {
        Flight flight = Flight.create(
                "FC4001",
                "LIS",
                "FRA",
                LocalDateTime.of(2026, 10, 10, 10, 0),
                LocalDateTime.of(2026, 10, 10, 13, 0)
        );

        Flight saved = flightRepository.saveAndFlush(flight);

        assertThat(saved.getId()).isNotNull();

        Flight found = flightRepository
                .findById(saved.getId())
                .orElseThrow();

        assertThat(found.getFlightNumber()).isEqualTo("FC4001");
        assertThat(found.getOrigin()).isEqualTo("LIS");
        assertThat(found.getDestination()).isEqualTo("FRA");
    }

    @Test
    void shouldRejectDuplicateFlightNumberAndDepartureTime() {
        LocalDateTime departure =
                LocalDateTime.of(2026, 10, 10, 10, 0);

        Flight firstFlight = Flight.create(
                "FC5001",
                "LIS",
                "FRA",
                departure,
                LocalDateTime.of(2026, 10, 10, 13, 0)
        );

        Flight secondFlight = Flight.create(
                "FC5001",
                "LIS",
                "MAD",
                departure,
                LocalDateTime.of(2026, 10, 10, 12, 0)
        );

        flightRepository.saveAndFlush(firstFlight);

        assertThatThrownBy(() ->
                flightRepository.saveAndFlush(secondFlight)
        ).isInstanceOf(DataIntegrityViolationException.class);
    }
}