package com.github.bernabaris.flightsearchapi;

import com.github.bernabaris.flightsearchapi.dto.DtoUtils;
import com.github.bernabaris.flightsearchapi.dto.UserInputDto;
import com.github.bernabaris.flightsearchapi.impl.DBServiceImpl;
import com.github.bernabaris.flightsearchapi.impl.FlightServiceImpl;
import com.github.bernabaris.flightsearchapi.impl.UserServiceImpl;
import com.github.bernabaris.flightsearchapi.model.Flight;
import com.github.bernabaris.flightsearchapi.service.FlightService;
import com.github.bernabaris.flightsearchapi.service.UserService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {FlightSearchApiApplication.class, FlightServiceImpl.class, UserServiceImpl.class, DBServiceImpl.class},
        loader = AnnotationConfigContextLoader.class
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestPropertySource(locations = "classpath:test.properties")
@DataJpaTest(showSql = false)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
@Slf4j
public class FlightBoxTest {

    @Value("${defaults.admin.email:admin@musebox.com}")
    private String defaultAdminEmail;

    @Resource
    private FlightService flightService;

    @Resource
    private UserService userService;

    UserInputDto user1InputDto = UserInputDto.builder()
            .firstName("Test")
            .lastName("User")
            .email("testuser@flightbox.com")
            .password("testuser")
            .build();

    Date now = new Date();
    LocalDateTime localNow = Instant.ofEpochMilli(now.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    FlightInputDto flight1dto = FlightInputDto.builder()
            .departureAirportId(1L)
            .arrivalAirportId(2L)
            .departureDateTime(localNow)
            .returnDateTime(localNow.plusDays(1))
            .build();

    @org.junit.Test
    @Rollback(false)
    public void test01_addUser() {
        userService.signUp(DtoUtils.convertToUser(user1InputDto));
        Assert.assertNotNull(userService.fetchUserByEmail(user1InputDto.getEmail()));
    }

    static Flight flight1;
    @Test
    @Rollback(false)
    public void test02_addFlight() {
        Flight addedFlight = flightService.addFlight(DtoUtils.convertToFlight(flight1dto));
        flight1 = addedFlight;
        Assert.assertNotNull(addedFlight);
        Assert.assertEquals(flight1dto.getDepartureAirportId(), addedFlight.getDepartureAirportId());
        Assert.assertEquals(flight1dto.getArrivalAirportId(), addedFlight.getArrivalAirportId());
    }

    @Test
    @Rollback(false)
    public void test03_deleteFlight() {
        Flight deletedFlight = flightService.deleteFlight(flight1.getId());
        Assert.assertNotNull(deletedFlight);
        Assert.assertNull(flightService.getFlight(flight1.getId()));
        Assert.assertTrue(flightService.getAirport(flight1.getArrivalAirportId()).isPresent());
        Assert.assertTrue(flightService.getAirport(flight1.getDepartureAirportId()).isPresent());
    }
}
