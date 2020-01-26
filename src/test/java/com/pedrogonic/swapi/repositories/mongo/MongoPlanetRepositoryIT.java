package com.pedrogonic.swapi.repositories.mongo;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.domain.Planet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    @BeforeEach
    void setUp() {
    }

    @Disabled
    @Test
    void findAll() {
    }

    @Disabled
    @Test
    void findById() {
    }

    @Test
    void save() {
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

    @Disabled
    @Test
    void insert() {
    }

    @Disabled
    @Test
    void deleteById() {
    }
}