package com.github.bernabaris.flightsearchapi.service;

import com.github.bernabaris.flightsearchapi.entity.AirportEntity;
import com.github.bernabaris.flightsearchapi.model.Airport;
import com.github.bernabaris.flightsearchapi.model.AppUser;
import com.github.bernabaris.flightsearchapi.model.Flight;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DBService {

    Optional<AppUser> getUserByEmail(String email);

    AppUser addOrUpdateUser(AppUser user);

    List<Airport> getAllAirports();

    Optional<Airport> getAirport(long id);

    List<Flight> getAllFlights();

    Flight addOrUpdateFlight(Flight flight);

    Optional<Flight> getFlightById(long flightId);

    List<Airport> getAirportsByFlight(long flightId);

    Airport addOrUpdateAirport(Airport airport);
    List<Flight> getFlightsByCriteria(AirportEntity departureAirport, AirportEntity arrivalAirport, LocalDateTime departureDate, LocalDateTime returnDate);

    void deleteFlight(long flightId);
}