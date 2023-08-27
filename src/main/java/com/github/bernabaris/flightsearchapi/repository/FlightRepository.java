package com.github.bernabaris.flightsearchapi.repository;

import com.github.bernabaris.flightsearchapi.entity.FlightEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<FlightEntity,Long> {
    @Query("SELECT f FROM FlightEntity f WHERE " +
            "f.departureAirport.id = :departureAirportId AND " +
            "f.arrivalAirport.id = :arrivalAirportId AND " +
            "f.date = :date")
    List<FlightEntity> findByCriteria(@Param("departureAirport") long departureAirportId,
                                      @Param("arrivalAirport") long arrivalAirportId,
                                      @Param("date") LocalDateTime date);
}
