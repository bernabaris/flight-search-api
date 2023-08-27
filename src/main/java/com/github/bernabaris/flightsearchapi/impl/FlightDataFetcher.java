package com.github.bernabaris.flightsearchapi.impl;

import com.github.bernabaris.flightsearchapi.entity.AirportEntity;
import com.github.bernabaris.flightsearchapi.entity.CityEntity;
import com.github.bernabaris.flightsearchapi.entity.FlightEntity;
import com.github.bernabaris.flightsearchapi.model.Flight;
import com.github.bernabaris.flightsearchapi.repository.FlightRepository;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightDataFetcher {

    private final DataGenerator dataGenerator;
    private final FlightRepository flightRepository;

    public FlightDataFetcher(FlightRepository flightRepository, DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
        this.flightRepository = flightRepository;
    }
    
    @PostConstruct
    public void init() {
        fetchAndSaveFlightData(); // for init
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
                .date(flight.getFlightDate())
                .price(flight.getPrice())
                .build();
    }

    private List<Flight> mockApiCall() {
        return dataGenerator.getFlights();
    }
}
