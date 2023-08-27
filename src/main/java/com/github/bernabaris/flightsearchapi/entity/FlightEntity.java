package com.github.bernabaris.flightsearchapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "departure_airport_id")
    private AirportEntity departureAirport;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "arrival_airport_id")
    private AirportEntity arrivalAirport;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private UserEntity createdBy;

    private LocalDateTime departureDateTime;

    private LocalDateTime returnDateTime;

    private BigDecimal price;

}
