package com.github.bernabaris.flightsearchapi.service;

import com.github.bernabaris.flightsearchapi.entity.AirportEntity;
import com.github.bernabaris.flightsearchapi.entity.CityEntity;
import com.github.bernabaris.flightsearchapi.entity.FlightEntity;
import com.github.bernabaris.flightsearchapi.model.Flight;
import com.github.bernabaris.flightsearchapi.repository.FlightRepository;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightDataFetcherService {

    private final FlightRepository flightRepository;

    public FlightDataFetcherService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Scheduled(cron = "0 0 * * * ?")  // Run every hour
    public void fetchAndSaveFlightData() {
        List<Flight> flights = mockApiCall();

        List<FlightEntity> flightEntities = flights.stream()
                .map(this::convertToFlightEntity)
                .collect(Collectors.toList());

        flightRepository.saveAll(flightEntities);
    }

    private FlightEntity convertToFlightEntity(Flight flight) {
        AirportEntity departureAirportEntity = new AirportEntity();
        departureAirportEntity.setId(flight.getDepartureAirportId());
        CityEntity departureCity = new CityEntity();
        departureCity.setName(flight.getDepartureAirport());
        departureAirportEntity.setCity(departureCity);

        AirportEntity arrivalAirportEntity = new AirportEntity();
        arrivalAirportEntity.setId(flight.getArrivalAirportId());
        CityEntity arrivalCity = new CityEntity();
        arrivalCity.setName(flight.getArrivalAirport());
        arrivalAirportEntity.setCity(arrivalCity);

        return FlightEntity.builder()
                .id(flight.getId())
                .departureAirport(departureAirportEntity)
                .arrivalAirport(arrivalAirportEntity)
                .departureDateTime(flight.getDepartureDateTime())
                .returnDateTime(flight.getReturnDateTime())
                .price(flight.getPrice())
                .build();
    }

    private List<Flight> mockApiCall() {
        List<Flight> flights = new ArrayList<>();
        flights.add(Flight.builder()
                .departureAirport("NYC")
                .arrivalAirport("LAX")
                .departureDateTime(LocalDateTime.now())
                .returnDateTime(LocalDateTime.now().plusHours(6))
                .price(new BigDecimal("300.00"))
                .build());
        return flights;
    }
}
