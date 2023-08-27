package com.github.bernabaris.flightsearchapi.impl;

import com.github.bernabaris.flightsearchapi.model.Flight;
import com.github.bernabaris.flightsearchapi.model.Flights;
import com.github.bernabaris.flightsearchapi.util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class DataGenerator {
    private static final String FLIGHT_JSON_FILE = "flight.json";

    private final ResourceLoader resourceLoader;

    @Value("${data.directory:classpath:data}")
    private String dataDirectory;
    
    public DataGenerator(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public static <T> T loadJsonFile(ResourceLoader resourceLoader, String fileDirectory, String filename,
                                     Class<T> classOfT) throws IOException {
        log.info("Loading json file: {} from directory: {}", filename, fileDirectory);
        Resource resource = resourceLoader.getResource(fileDirectory.concat("/").concat(filename));
        if (resource.exists()) {
            Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            return gson.fromJson(FileCopyUtils.copyToString(reader), classOfT);
        } else {
            log.warn("Resource is not found. directory:{} file:{}", fileDirectory, filename);
            return null;
        }
    }

    public List<Flight> getFlights() {
        Flights flights = null;
        try {
            flights = DataGenerator.loadJsonFile(resourceLoader, dataDirectory, FLIGHT_JSON_FILE, Flights.class);
            if (flights != null) {
                flights.setFlightList(flights.getFlightList());
            } else {
                log.error("Flight file is invalid. Data directory: {}", dataDirectory);
                throw new IOException("Flight file is invalid");
            }
            log.info("Flight data loaded.");
        } catch (IOException e) {
            log.error("Error occurred while loading flight json file.", e);
        }
        assert flights != null;
        return flights.getFlightList();
    }
}
