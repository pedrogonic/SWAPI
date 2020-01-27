package com.pedrogonic.swapi.services.impl;

import com.pedrogonic.swapi.application.components.Messages;
import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.repositories.IPlanetRepository;
import com.pedrogonic.swapi.services.ISwapiService;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {

    @Mock
    IPlanetRepository planetRepository;

    @Mock
    Messages messages;

    @Mock
    ISwapiService swapiService;

    @InjectMocks
    PlanetService planetService;


    final String EXISTING_PLANET_NAME = "Tatooine";
    final int EXISTING_PLANET_FILM_COUNT = 5;

    ObjectId objectid;
    Planet planet;
    Planet swapiPlanet;
    List<Planet> planets;
    Pageable pageable;
    PlanetFilter planetFilter;

    @BeforeEach
    void setUp() {
        planets = new ArrayList<>();
        objectid = ObjectId.get();
        planet = Planet.builder()
                .id(objectid.toString())
                .name(EXISTING_PLANET_NAME)
                .climate("user input")
                .terrain("user input")
                .build();
        swapiPlanet = Planet.builder()
                .id(objectid.toString())
                .name(EXISTING_PLANET_NAME)
                .climate("arid")
                .terrain("desert")
                .filmCount(EXISTING_PLANET_FILM_COUNT)
                .build();
        planets.add(planet);

        pageable = PageRequest.of(0, 10);
    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    @DisplayName("Service - Find all")
    void findAll() {
        given(planetRepository.findAll(pageable, planetFilter)).willReturn(planets);
        given(swapiService.findPlanetByName(any())).willReturn(swapiPlanet);

        assertEquals(planets.size(), planetService.findAll(pageable, planetFilter).size());
    }

    @SneakyThrows
    @Test
    @DisplayName("Service - Find by existing ID")
    void findByExistingId() {
        given(planetRepository.findById(objectid.toString())).willReturn(Optional.of(planet));
        given(swapiService.findPlanetByName(EXISTING_PLANET_NAME)).willReturn(swapiPlanet);

        Planet foundPlanet = planetService.findById(planet.getId());

        assertEquals(swapiPlanet.getFilmCount(), foundPlanet.getFilmCount());
        assertEquals(planet.getName(), foundPlanet.getName());
        assertEquals(planet.getTerrain(), foundPlanet.getTerrain());
        assertEquals(planet.getClimate(), foundPlanet.getClimate());
    }

    @Test
    @DisplayName("Service - Find by non existing ID")
    void findByNonExistingId() {
        given(planetRepository.findById("NON_EXISTING_ID")).willReturn(Optional.empty());

        assertThrows(PlanetNotFoundException.class, () -> planetService.findById("NON_EXISTING_ID"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Service - update planet")
    void updatePlanet() {
        given(swapiService.findPlanetByName(EXISTING_PLANET_NAME)).willReturn(swapiPlanet);
        given(planetRepository.findById(objectid.toString())).willReturn(Optional.of(planet));

        Planet returnedPlanet = Planet.builder()
                .id(planet.getId())
                .name(planet.getName())
                .climate(planet.getClimate())
                .terrain(planet.getTerrain())
                .filmCount(swapiPlanet.getFilmCount())
                .build();
        given(planetRepository.save(planet)).willReturn(planet);

        assertEquals(returnedPlanet, planetService.updatePlanet(planet));
    }

    @SneakyThrows
    @Test
    @DisplayName("Service - create valid planet")
    void createPlanet() {
        given(swapiService.findPlanetByName(EXISTING_PLANET_NAME)).willReturn(swapiPlanet);

        Planet returnedPlanet = Planet.builder()
                                    .id(planet.getId())
                                    .name(planet.getName())
                                    .climate(planet.getClimate())
                                    .terrain(planet.getTerrain())
                                    .filmCount(swapiPlanet.getFilmCount())
                                .build();
        given(planetRepository.insert(planet)).willReturn(planet);

        assertEquals(returnedPlanet, planetService.createPlanet(planet));
    }

}