package com.github.bernabaris.flightsearchapi.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class FlightDto {
    private Long id;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureDateTime;
    private LocalDateTime returnDateTime;
    private BigDecimal price;
    private String createdBy;
}
