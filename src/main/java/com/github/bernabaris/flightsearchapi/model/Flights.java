package com.github.bernabaris.flightsearchapi.model;

import lombok.Data;

import java.util.List;

@Data
public class Flights {
    private List<Flight> flightList;
}
