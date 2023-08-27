package com.github.bernabaris.flightsearchapi.impl;

import com.github.bernabaris.flightsearchapi.entity.AirportEntity;
import com.github.bernabaris.flightsearchapi.entity.CityEntity;
import com.github.bernabaris.flightsearchapi.entity.FlightEntity;
import com.github.bernabaris.flightsearchapi.entity.UserEntity;
import com.github.bernabaris.flightsearchapi.model.Airport;
import com.github.bernabaris.flightsearchapi.model.Flight;
import com.github.bernabaris.flightsearchapi.model.AppUser;
import com.github.bernabaris.flightsearchapi.repository.AirportRepository;
import com.github.bernabaris.flightsearchapi.repository.FlightRepository;
import com.github.bernabaris.flightsearchapi.repository.UserRepository;
import com.github.bernabaris.flightsearchapi.service.DBService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DBServiceImpl implements DBService {

    private final UserRepository userRepository;
    private final AirportRepository airportRepository;
    private final FlightRepository flightRepository;

    DBServiceImpl(UserRepository userRepository, AirportRepository airportRepository, FlightRepository flightRepository) {
        this.userRepository = userRepository;
        this.airportRepository = airportRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public Optional<AppUser> getUserByEmail(String email) {
        UserEntity userEntity = userRepository.fetchUserByEmailId(email);
        return userEntity == null ? Optional.empty() : Optional.of(convertToUser(userEntity));
    }

    @Override
    public AppUser addOrUpdateUser(AppUser user) {
        return convertToUser(userRepository.save(convertToUserEntity(user)));
    }

    @Override
    public Optional<Flight> getFlightById(long flightId) {
        Optional<FlightEntity> optFlight = flightRepository.findById(flightId);
        return optFlight.map(this::convertToFlight);
    }

    @Override
    public List<Flight> getFlightsByCriteria(AirportEntity departureAirport, AirportEntity arrivalAirport, LocalDateTime departureDate, LocalDateTime returnDate) {
        List<FlightEntity> flights = flightRepository.findByCriteria(departureAirport, arrivalAirport, departureDate, returnDate);
        return flights.stream().map(this::convertToFlight).collect(Collectors.toList());
    }


    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll().stream().map(this::convertToFlight).toList();
    }



    @Override
    public void deleteFlight(long flightId) {
        flightRepository.deleteById(flightId);
    }

    @Override
    public Optional<Airport> getAirport(long id) {
        Optional<AirportEntity> optAirport = airportRepository.findById(id);
        return optAirport.map(this::convertToAirport);
    }

    @Override
    public List<Airport> getAirportsByFlight(long flightId) {
        return airportRepository.fetchAirportsByFlightId(flightId).stream().map(this::convertToAirport).toList();
    }

    @Override
    public Airport addOrUpdateAirport(Airport airport) {
        return convertToAirport(airportRepository.save(convertToAirportEntity(airport)));
    }

    @Override
    public List<Airport> getAllAirports() {
        return airportRepository.findAll().stream().map(this::convertToAirport).toList();
    }
    @Override
    public Flight addOrUpdateFlight(Flight flight) {
        return convertToFlight(flightRepository.save(convertToFlightEntity(flight)));
    }

    private AppUser convertToUser(UserEntity userEntity) {
        return AppUser.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .admin(userEntity.isAdmin())
                .active(userEntity.isActive())
                .created(userEntity.getCreated())
                .build();
    }

    private UserEntity convertToUserEntity(AppUser user) {
        return UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .admin(user.isAdmin())
                .active(user.isActive())
                .created(user.getCreated())
                .build();
    }

    private Flight convertToFlight(FlightEntity flightEntity) {

        return Flight.builder()
                .id(flightEntity.getId())
                .departureAirport(flightEntity.getDepartureAirport().getCity().getName())
                .arrivalAirport(flightEntity.getArrivalAirport().getCity().getName())
                .departureDateTime(flightEntity.getDepartureDateTime())
                .returnDateTime(flightEntity.getReturnDateTime())
                .price(flightEntity.getPrice())
                .departureAirportId(flightEntity.getDepartureAirport().getId())
                .arrivalAirportId(flightEntity.getArrivalAirport().getId())
                .build();
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


    private Airport convertToAirport(AirportEntity airportEntity) {
        return Airport.builder()
                .id(airportEntity.getId())
                .city(airportEntity.getCity().getName())  // Assuming Airport model has a city attribute
                // Add any other fields that need conversion here
                .build();
    }

    private AirportEntity convertToAirportEntity(Airport airport) {

        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(airport.getCity());

        return AirportEntity.builder()
                .id(airport.getId())
                .city(cityEntity)  // Now we provide a CityEntity object
                .build();
    }
}






