package com.pedrogonic.swapi.controllers;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.http.RequestPlanetDTO;
import com.pedrogonic.swapi.model.dtos.http.ResponsePlanetDTO;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.services.IPlanetService;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PlanetControllerTest {

    @Mock
    IPlanetService planetService;

    @Mock
    OrikaMapper orikaMapper;

    @Mock
    PlanetResourceAssembler assembler;

    @InjectMocks
    PlanetController planetController;

    MockMvc mockMvc;


    private static String BASE_PATH = "http://localhost/planets";

    Planet planet;
    RequestPlanetDTO requestPlanetDTO;
    ResponsePlanetDTO responsePlanetDTO;
    EntityModel<ResponsePlanetDTO> model;
    ObjectId objectid;
    List<Planet> planets;
    List<ResponsePlanetDTO> planetDTOList;
    int page;
    int size;
    String queryName;
    Pageable pageable;
    PlanetFilter planetFilter;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        planets = new ArrayList<>();
        objectid = ObjectId.get();
        planet = Planet.builder()
                    .id(objectid.toString())
                    .name("Tatooine")
                    .climate("arid")
                    .terrain("desert")
                    .filmCount(5)
                .build();
        planets.add(planet);

        requestPlanetDTO = RequestPlanetDTO.builder()
                    .name("Tatooine")
                    .climate("arid")
                    .terrain("desert")
                .build();

        planetDTOList = new ArrayList<>();
        responsePlanetDTO = ResponsePlanetDTO.builder()
                    .id(objectid.toString())
                    .name("Tatooine")
                    .climate("arid")
                    .terrain("desert")
                    .filmCount(5)
                .build();
        planetDTOList.add(responsePlanetDTO);

        pageable = PageRequest.of(0, 10);

        model = new EntityModel<>(responsePlanetDTO);

        mockMvc = MockMvcBuilders.standaloneSetup(planetController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    @DisplayName("GET planets/")
    void listAllWithNoArguments() {
        planetFilter = PlanetFilter.builder().build();
        given(orikaMapper.mapAsList(planets, ResponsePlanetDTO.class)).willReturn(planetDTOList);
        given(planetService.findAll(pageable, planetFilter)).willReturn(planets);
        given(assembler.toModel(any())).willReturn(model);

        final ResultActions result = mockMvc.perform(get("/planets"));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                ;
        result
                .andExpect(jsonPath("content.length()", is(1)))
                .andExpect(jsonPath("links[0].rel", is("self")))
                .andExpect(jsonPath("links[0].href", is(BASE_PATH)))
                ;
        result
                .andExpect(jsonPath("content[0].id", is(planet.getId())))
                .andExpect(jsonPath("content[0].name", is(planet.getName())))
                .andExpect(jsonPath("content[0].terrain", is(planet.getTerrain())))
                .andExpect(jsonPath("content[0].climate", is(planet.getClimate())))
                .andExpect(jsonPath("content[0].filmCount", is(planet.getFilmCount())))
                ;

    }

    @SneakyThrows
    @Test
    @DisplayName("GET planets/?name={existingPlanetName}")
    void searchByExistingPlanetName() {
        final String QUERIED_NAME = "Tatooine";
        planetFilter = PlanetFilter.builder().name(QUERIED_NAME).build();
        given(orikaMapper.mapAsList(planets, ResponsePlanetDTO.class)).willReturn(planetDTOList);
        given(planetService.findAll(pageable, planetFilter)).willReturn(planets);
        given(assembler.toModel(any())).willReturn(model);

        final ResultActions result = mockMvc.perform(get("/planets")
                                                        .queryParam("name",QUERIED_NAME));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                ;
        result
                .andExpect(jsonPath("content.length()", is(1)))
                ;
        result
                .andExpect(jsonPath("content[0].id", is(planet.getId())))
                .andExpect(jsonPath("content[0].name", is(planet.getName())))
                .andExpect(jsonPath("content[0].terrain", is(planet.getTerrain())))
                .andExpect(jsonPath("content[0].climate", is(planet.getClimate())))
                .andExpect(jsonPath("content[0].filmCount", is(planet.getFilmCount())))
                ;
    }

    @SneakyThrows
    @Test
    @DisplayName("GET planets/?name={nonExistingPlanetName}")
    void searchByNonExistingPlanetName() {
        final String QUERIED_NAME = "Naboo";pageable = PageRequest.of(0, 10);
        planetFilter = PlanetFilter.builder().name(QUERIED_NAME).build();
        given(orikaMapper.mapAsList(new ArrayList<>(), ResponsePlanetDTO.class)).willReturn(new ArrayList<>());
        given(planetService.findAll(pageable, planetFilter)).willReturn(new ArrayList<>());

        final ResultActions result = mockMvc.perform(get("/planets")
                .queryParam("name",QUERIED_NAME));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        ;
        result
                .andExpect(jsonPath("content.length()", is(0)))
        ;
    }

    @Disabled
    @Test
    void searchById() {
    }

    @Disabled
    @Test
    void whenPutRequestToPlanetsAndValidPlanet() {
    }

    @Disabled
    @Test
    void whenPutRequestToPlanetsAndInvalidPlanet() {
    }

    @Disabled
    @Test
    void whenPostRequestToPlanetsAndValidPlanet() {
    }

    @Disabled
    @Test
    void whenPostRequestToPlanetsAndInvalidPlanet() {
    }

    @Disabled
    @Test
    void deletePlanet() {
    }
}