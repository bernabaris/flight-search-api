package com.github.bernabaris.flightsearchapi.controller;

import com.github.bernabaris.flightsearchapi.dto.FlightDto;
import com.github.bernabaris.flightsearchapi.dto.FlightSearchInputDto;
import com.github.bernabaris.flightsearchapi.dto.FlightSearchResultDto;
import com.github.bernabaris.flightsearchapi.model.Airport;
import com.github.bernabaris.flightsearchapi.model.Flight;
import com.github.bernabaris.flightsearchapi.dto.DtoUtils;
import com.github.bernabaris.flightsearchapi.model.AppUser;
import com.github.bernabaris.flightsearchapi.service.FlightService;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("flight")
@Slf4j
public class FlightController {

    private final FlightService flightService;

    FlightController(FlightService flightService) {
        this.flightService = flightService;
    }


    @GetMapping(value = "/airport")
    public List<Airport> getAllAirports() {
        return flightService.getAllAirports().stream().filter(l -> l.getId() != 0).toList();
    }

    @GetMapping
    public List<FlightDto> getAllFlights() {
        log.info("Fetching all flights");
        return flightService.getAllFlights().stream().map(DtoUtils::convertToFlightDto).toList();
    }

    @GetMapping("/search")
    public FlightSearchResultDto getFlights(@RequestBody FlightSearchInputDto flightSearchInputDto) {
        FlightSearchResultDto.FlightSearchResultDtoBuilder builder = FlightSearchResultDto.builder();
        builder.arrivalFlights(flightService.getFlightsByCriteria(flightSearchInputDto.getDepartureAirportId(),
                        flightSearchInputDto.getArrivalAirportId(), flightSearchInputDto.getDepartureDateTime())
                .stream().map(DtoUtils::convertToFlightDto).toList());
        if (flightSearchInputDto.getReturnDateTime() != null) {
            builder.returnFlights(flightService.getFlightsByCriteria(flightSearchInputDto.getDepartureAirportId(),
                            flightSearchInputDto.getArrivalAirportId(), flightSearchInputDto.getReturnDateTime())
                    .stream().map(DtoUtils::convertToFlightDto).toList());
        }
        return builder.build();
    }
}
