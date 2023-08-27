package com.github.bernabaris.flightsearchapi.impl;

import com.github.bernabaris.flightsearchapi.entity.AirportEntity;
import com.github.bernabaris.flightsearchapi.model.Airport;
import com.github.bernabaris.flightsearchapi.model.Flight;
import com.github.bernabaris.flightsearchapi.service.DBService;
import com.github.bernabaris.flightsearchapi.service.FlightService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class FlightServiceImpl implements FlightService {

    @Value("${defaults.admin.email}")
    private String defaultAdminEmail;

    private final DBService dbService;

    FlightServiceImpl(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public List<Airport> getAllAirports() {
        return dbService.getAllAirports();
    }

    @Override
    public Optional<Airport> getAirport(long id) {
        return dbService.getAirport(id);
    }

    @Override
    public List<Flight> getAllFlights() {
        return dbService.getAllFlights();
    }

    @Override
    public Flight addFlight(Flight flight) {
        flight.setCreated(new Date());
        flight.setUpdated(new Date());
        return dbService.addOrUpdateFlight(flight);
    }

    @Override
    public Optional<Flight> getFlight(long flightId) {
        return dbService.getFlightById(flightId);
    }

    @Override
    public Flight deleteFlight(long flightId) {
        Optional<Flight> flightOptional = dbService.getFlightById(flightId);
        if (flightOptional.isEmpty()) {
            return null;
        }
        dbService.deleteFlight(flightId);
        return flightOptional.get();

    }

    @Override
    public List<Flight> getFlightsByCriteria(AirportEntity departureAirport, AirportEntity arrivalAirport, LocalDateTime departureDate, LocalDateTime returnDate) {
        return dbService.getFlightsByCriteria(departureAirport, arrivalAirport, departureDate, returnDate);
    }
}
