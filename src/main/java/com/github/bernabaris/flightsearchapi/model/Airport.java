package com.github.bernabaris.flightsearchapi.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Model for representing an Airport")
public class Airport {

    @Schema(description = "Unique id field of airport object")
    private Long id;

    @Schema(description = "Name of the airport")
    private String city;
}
