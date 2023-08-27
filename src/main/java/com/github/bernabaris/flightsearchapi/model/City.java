package com.github.bernabaris.flightsearchapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Model for representing a City")
public class City {

    @Schema(description = "Unique id field of city object")
    private Long id;

    @Schema(description = "Name of the city")
    private String name;

    @Schema(description = "Country where the city is located")
    private String country;
}
