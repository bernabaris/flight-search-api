package com.github.bernabaris.flightsearchapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "airport")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id")
    private CityEntity city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
