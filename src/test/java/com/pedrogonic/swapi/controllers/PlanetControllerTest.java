package com.pedrogonic.swapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.GlobalExceptionHandler;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PlanetControllerTest {

    ObjectMapper mapper = new ObjectMapper();

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
    RequestPlanetDTO validRequestPlanetDTO;
    RequestPlanetDTO invalidRequestPlanetDTO;
    ResponsePlanetDTO responsePlanetDTO;
    EntityModel<ResponsePlanetDTO> model;
    ObjectId objectid;
    List<Planet> planets;
    List<ResponsePlanetDTO> planetDTOList;
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

        validRequestPlanetDTO = RequestPlanetDTO.builder()
                    .name("Tatooine")
                    .climate("arid")
                    .terrain("desert")
                .build();

        invalidRequestPlanetDTO = RequestPlanetDTO.builder()
                .name("")
                .climate("")
                .terrain("")
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

        mockMvc = MockMvcBuilders.standaloneSetup(planetController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();
    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    @DisplayName("GET planets/")
    void whenListAllWithNoArguments() {
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
    void whenSearchByExistingPlanetName() {
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
    void whenSearchByNonExistingPlanetName() {
        final String QUERIED_NAME = "Naboo";
        pageable = PageRequest.of(0, 10);
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

    private void verifyJson(final ResultActions result) throws Exception {
        result
                .andExpect(jsonPath("id", is(planet.getId())))
                .andExpect(jsonPath("name", is(planet.getName())))
                .andExpect(jsonPath("terrain", is(planet.getTerrain())))
                .andExpect(jsonPath("climate", is(planet.getClimate())))
                .andExpect(jsonPath("filmCount", is(planet.getFilmCount())))
                ;
    }

    @SneakyThrows
    @Test
    @DisplayName("GET planets/{existingPlanetId}")
    void whenSearchByExistingPlanetId() {
        given(orikaMapper.map(planet, ResponsePlanetDTO.class)).willReturn(responsePlanetDTO);
        given(planetService.findById(planet.getId())).willReturn(planet);
        given(assembler.toModel(any())).willReturn(model);

        final ResultActions result = mockMvc.perform(get("/planets/" + planet.getId()));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                ;
        verifyJson(result);
    }

    @SneakyThrows
    @Test
    @DisplayName("GET planets/{nonExistingPlanetId}")
    void whenSearchByNonExistingPlanetId() {
        final String ID = "NON_EXISTING_ID";
        final Exception exception = new PlanetNotFoundException("message");
        given(planetService.findById(ID)).willThrow(exception);

        final ResultActions result = mockMvc.perform(get("/planets/" + ID));

        result
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    @DisplayName("PUT planets/{existingPlanetId}")
    void whenPutRequestToPlanetsAndValidPlanet() {
        given(orikaMapper.map(planet, ResponsePlanetDTO.class)).willReturn(responsePlanetDTO);
        given(orikaMapper.map(validRequestPlanetDTO, Planet.class)).willReturn(planet);
        given(planetService.updatePlanet(planet)).willReturn(planet);
        given(assembler.toModel(any())).willReturn(model);

        final ResultActions result = mockMvc.perform(put("/planets/" + planet.getId())
                                                .content(mapper.writeValueAsString(validRequestPlanetDTO))
                                                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        ;
        verifyJson(result);
    }

    @SneakyThrows
    @Test
    @DisplayName("PUT planets/{existingPlanetId} - invalid planet")
    void whenPutRequestToPlanetsAndInvalidPlanet() {
        final ResultActions result = mockMvc.perform(put("/planets/" + planet.getId())
                .content(mapper.writeValueAsString(invalidRequestPlanetDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    @DisplayName("PUT planets/{nonExistingPlanetId}")
    void whenPutRequestByNonExistingPlanetId() {
        final String ID = "NON_EXISTING_ID";
        final Exception exception = new PlanetNotFoundException("message");
        given(orikaMapper.map(validRequestPlanetDTO, Planet.class)).willReturn(planet);
        given(planetService.updatePlanet(planet)).willThrow(exception);

        final ResultActions result = mockMvc.perform(put("/planets/" + ID)
                                                .content(mapper.writeValueAsString(validRequestPlanetDTO))
                                                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    @DisplayName("PUT planets/{existingPlanetId} - without body")
    void whenPutRequestWithoutPlanet() {
        final ResultActions result = mockMvc.perform(put("/planets/" + planet.getId()));

        result
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    @DisplayName("POST planets")
    void whenPostRequestToPlanetsAndValidPlanet() {
        given(orikaMapper.map(planet, ResponsePlanetDTO.class)).willReturn(responsePlanetDTO);
        given(orikaMapper.map(validRequestPlanetDTO, Planet.class)).willReturn(planet);
        given(planetService.createPlanet(planet)).willReturn(planet);
        given(assembler.toModel(any())).willReturn(model);

        final ResultActions result = mockMvc.perform(post("/planets")
                .content(mapper.writeValueAsString(validRequestPlanetDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        ;
        verifyJson(result);
    }

    @SneakyThrows
    @Test
    @DisplayName("POST planets - invalid planet")
    void whenPostRequestToPlanetsAndInvalidPlanet() {
        final ResultActions result = mockMvc.perform(post("/planets")
                .content(mapper.writeValueAsString(invalidRequestPlanetDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    @DisplayName("POST planets/{existingPlanetId} - without body")
    void whenPostRequestWithoutPlanet() {
        final ResultActions result = mockMvc.perform(post("/planets"));

        result
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    @DisplayName("DELETE planets/{id}")
    void deletePlanet() {
        final ResultActions result = mockMvc.perform(delete("/planets/" + planet.getId()));

        result
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
                ;
    }
}