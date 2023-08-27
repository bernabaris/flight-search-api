package com.github.bernabaris.flightsearchapi.service;


import com.github.bernabaris.flightsearchapi.entity.AirportEntity;
import com.github.bernabaris.flightsearchapi.model.Airport;
import com.github.bernabaris.flightsearchapi.model.Flight;
import com.github.bernabaris.flightsearchapi.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface FlightService {

    List<Airport> getAllAirports();
    Optional<Airport> getAirport(long Id);
    List<Flight> getAllFlights();
    Flight addFlight(Flight flight);
    Optional<Flight> getFlight(long flightId);

    Optional<List<Flight>> getFlightsByCriteria(AirportEntity departureAirport, AirportEntity arrivalAirport, LocalDateTime departureDate, LocalDateTime returnDate);

    Flight deleteFlight(long flightId);





}
