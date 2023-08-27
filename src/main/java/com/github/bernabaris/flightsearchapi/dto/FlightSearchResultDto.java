package com.github.bernabaris.flightsearchapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FlightSearchResultDto {
    List<FlightDto> arrivalFlights;
    List<FlightDto> returnFlights;
}
