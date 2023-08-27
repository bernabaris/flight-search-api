package com.github.bernabaris.flightsearchapi.controller;

import com.github.bernabaris.flightsearchapi.dto.FlightDto;
import com.github.bernabaris.flightsearchapi.dto.FlightInputDto;
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

@RestController
@RequestMapping("api/flight")
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

    @PostMapping
    public FlightDto addFlight(@RequestBody FlightInputDto flightInputDto, HttpServletResponse httpServletResponse, @AuthenticationPrincipal AppUser user) throws IOException {
        log.info("Add flight: {} user: {}", flightInputDto, user.getEmail());
        Flight flightToAdd = DtoUtils.convertToFlight(flightInputDto);
        flightToAdd.setCreatedBy(user.getEmail());
        Flight addedFlight = flightService.addFlight(flightToAdd);
        if (addedFlight == null) {
            httpServletResponse.sendError(HttpServletResponse.SC_CONFLICT, "Flight already exists");
            return null;
        }
        return DtoUtils.convertToFlightDto(addedFlight);
    }
}
