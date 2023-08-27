package com.github.bernabaris.flightsearchapi.repository;

import com.github.bernabaris.flightsearchapi.entity.AirportEntity;
import com.github.bernabaris.flightsearchapi.entity.FlightEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<FlightEntity,Long> {
    @Query("SELECT f FROM FlightEntity f WHERE " +
            "f.departureAirport = :departureAirport AND " +
            "f.arrivalAirport = :arrivalAirport AND " +
            "f.departureDateTime >= :departureDate AND " +
            "(f.returnDateTime <= :returnDate OR f.returnDateTime IS NULL)")
    List<FlightEntity> findByCriteria(@Param("departureAirport") AirportEntity departureAirport,
                                      @Param("arrivalAirport") AirportEntity arrivalAirport,
                                      @Param("departureDate") LocalDateTime departureDate,
                                      @Param("returnDate") LocalDateTime returnDate);
}
