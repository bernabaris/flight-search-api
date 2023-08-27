package com.github.bernabaris.flightsearchapi.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FlightInputDto {
    private Long departureAirportId;
    private Long arrivalAirportId;
    private LocalDateTime departureDateTime;
    private LocalDateTime returnDateTime;
}
