package com.github.bernabaris.flightsearchapi.service;

import com.github.bernabaris.flightsearchapi.model.Airport;
import com.github.bernabaris.flightsearchapi.model.Flight;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface FlightService {

    List<Airport> getAllAirports();
    Optional<Airport> getAirport(long id);
    List<Flight> getAllFlights();
    Flight addFlight(Flight flight);
    Optional<Flight> getFlight(long flightId);
    List<Flight> getFlightsByCriteria(long departureAirportId, long arrivalAirportId, LocalDateTime date);
    Flight deleteFlight(long flightId);
}
