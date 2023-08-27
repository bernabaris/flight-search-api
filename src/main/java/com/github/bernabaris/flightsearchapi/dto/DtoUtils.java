package com.github.bernabaris.flightsearchapi.dto;

import com.github.bernabaris.flightsearchapi.model.Flight;
import com.github.bernabaris.flightsearchapi.model.AppUser;

public class DtoUtils {
    private DtoUtils(){

    }

    public static Flight convertToFlight(FlightInputDto dto){
        return Flight.builder()
                .departureAirport(getAirportNameFromId(dto.getDepartureAirportId()))
                .arrivalAirport(getAirportNameFromId(dto.getArrivalAirportId()))
                .departureDateTime(dto.getDepartureDateTime())
                .returnDateTime(dto.getReturnDateTime())
                .build();

    }

    public static FlightDto convertToFlightDto(Flight flight){
        return FlightDto.builder()
                .id(flight.getId())
                .departureAirport(flight.getDepartureAirport())
                .arrivalAirport(flight.getArrivalAirport())
                .departureDateTime(flight.getDepartureDateTime())
                .returnDateTime(flight.getReturnDateTime())
                .price(flight.getPrice())
                .build();
    }

    public static AppUser convertToUser(UserInputDto dto) {
        return AppUser.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    public static UserDto convertToUserDto(AppUser user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .admin(user.isAdmin())
                .active(user.isActive())
                .created(user.getCreated())
                .build();
    }

    private static String getAirportNameFromId(Long id) {
        return "Airport-" + id;
    }
}
