package com.pedrogonic.swapi.repositories.mongo;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.db.mongo.MongoPlanet;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureBefore(MongoAutoConfiguration.class)

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestPropertySource(locations= "classpath:application-test.properties")
class MongoPlanetRepositoryIT {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    OrikaMapper orikaMapper;

    @Autowired
    MongoPlanetRepository mongoPlanetRepository;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(MongoPlanet.class);
    }

    @Test
    @DisplayName("Find all")
    void findAll() {
        Planet planet = Planet.builder()
                .name("Tatooine")
                .climate("arid")
                .terrain("desert")
                .build();

        mongoPlanetRepository.insert(planet);

        Pageable pageable = PageRequest.of(0, 10);
        PlanetFilter planetFilter = PlanetFilter.builder().build();

        assertEquals(1, mongoPlanetRepository.findAll(pageable, planetFilter).size());
    }

    @Test
    @DisplayName("Find by existing name")
    void findByExistingName() {
        final String PLANET_NAME = "Tatooine";
        final String PLANET_CLIMATE = "arid";
        final String PLANET_TERRAIN = "desert";
        Planet planet = Planet.builder()
                .name(PLANET_NAME)
                .climate(PLANET_CLIMATE)
                .terrain(PLANET_TERRAIN)
                .build();

        mongoPlanetRepository.insert(planet);

        Pageable pageable = PageRequest.of(0, 10);
        PlanetFilter planetFilter = PlanetFilter.builder().name(PLANET_NAME).build();

        List<Planet> result = mongoPlanetRepository.findAll(pageable, planetFilter);

        assertEquals(1, result.size());
        assertEquals(result.get(0).getName(), PLANET_NAME);
        assertEquals(result.get(0).getClimate(), PLANET_CLIMATE);
        assertEquals(result.get(0).getTerrain(), PLANET_TERRAIN);
    }

    @Test
    @DisplayName("Find by non existing name")
    void findByNonExistingName() {
        Planet planet = Planet.builder()
                .name("Tatooine")
                .climate("arid")
                .terrain("desert")
                .build();

        mongoPlanetRepository.insert(planet);

        Pageable pageable = PageRequest.of(0, 10);
        PlanetFilter planetFilter = PlanetFilter.builder().name("Endor").build();

        List<Planet> result = mongoPlanetRepository.findAll(pageable, planetFilter);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Insert new, find by existing id and update existing")
    void insertAndSaveAndFindById() {
        Planet planet = Planet.builder()
                .name("Tatooine")
                .climate("arid")
                .terrain("desert")
                .build();

        Planet insertedPlanet = mongoPlanetRepository.insert(planet);

        insertedPlanet.setName("Alderaan");
        insertedPlanet.setClimate("temperate");
        insertedPlanet.setTerrain("grasslands, mountains");

        String id = insertedPlanet.getId();

        planet = Planet.builder()
                .id(id)
                .name(insertedPlanet.getName())
                .climate(insertedPlanet.getClimate())
                .terrain(insertedPlanet.getTerrain())
                .build();

        mongoPlanetRepository.save(planet);

        Planet updatedPlanet = mongoPlanetRepository.findById(id).get();

        assertEquals(insertedPlanet, updatedPlanet);
    }

    @Test
    @DisplayName("Insert invalid")
    void insertInvalid() {
        Planet planet = Planet.builder()
                .name("")
                .climate("")
                .terrain("")
                .build();

        assertThrows(ConstraintViolationException.class,
                () -> mongoPlanetRepository.insert(planet));
    }

    @Test
    @DisplayName("Find by non existing id")
    void findByNonExistingId() {
        String id = "NON_EXISTING_ID";
        assertFalse(mongoPlanetRepository.findById(id).isPresent());
    }

    @Test
    @DisplayName("Delete by id")
    void deleteById() {
        Planet planet = Planet.builder()
                .name("Tatooine")
                .climate("arid")
                .terrain("desert")
                .build();

        Planet insertedPlanet = mongoPlanetRepository.insert(planet);

        String id = insertedPlanet.getId();

        mongoPlanetRepository.deleteById(id);

        assertTrue(mongoPlanetRepository.findById(id).isEmpty());
    }
}