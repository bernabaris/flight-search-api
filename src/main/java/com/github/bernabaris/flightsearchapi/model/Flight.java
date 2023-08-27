package com.github.bernabaris.flightsearchapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@Schema(description = "Model for representing a Flight")
public class Flight {

    @Schema(description = "Unique id field of flight object")
    private Long id;

    @Schema(description = "Departure airport of the flight")
    private String departureAirport;

    @Schema(description = "Arrival airport of the flight")
    private String arrivalAirport;

    @Schema(description = "Departure date and time of the flight")
    private LocalDateTime departureDateTime;

    @Schema(description = "Arrival date and time of the flight")
    private LocalDateTime returnDateTime;

    @Schema(description = "Price of the flight")
    private BigDecimal price;

    @Schema(description = "ID of the departure airport")
    private Long departureAirportId;

    @Schema(description = "ID of the arrival airport")
    private Long arrivalAirportId;

    @Schema(description = "User who created the flight record")
    private String createdBy;

    @Schema(description = "Date when the flight record was created")
    private Date created;

    @Schema(description = "Date when the flight record was last updated")
    private Date updated;
}
