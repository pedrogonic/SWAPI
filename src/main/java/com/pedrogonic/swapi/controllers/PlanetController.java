package com.pedrogonic.swapi.controllers;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.application.exception.SwapiUnreachableException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.http.RequestPlanetDTO;
import com.pedrogonic.swapi.model.dtos.http.ResponsePlanetDTO;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.services.IPlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/planets")
public class PlanetController {

    @Autowired
    IPlanetService planetService;

    @Autowired
    OrikaMapper orikaMapper;


    @GetMapping(produces = "application/json; charset=UTF-8")
    List<ResponsePlanetDTO> listAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                    @RequestParam(required = false) final String name) throws SwapiUnreachableException {

        Pageable pageable = PageRequest.of(page, size);

        PlanetFilter planetFilter = PlanetFilter.builder()
                .name(name)
                .build();

        List<Planet> planets = planetService.findAll(pageable, planetFilter);

        return orikaMapper.mapAsList(planets, ResponsePlanetDTO.class);
    }

    @GetMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    ResponsePlanetDTO getById(@PathVariable final String id) throws PlanetNotFoundException, SwapiUnreachableException {


        Planet planet = planetService.findById(id);

        return orikaMapper.map(planet, ResponsePlanetDTO.class);
    }

    @PutMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    ResponseEntity<ResponsePlanetDTO> updatePlanet(@Valid @RequestBody RequestPlanetDTO requestPlanetDTO, @PathVariable String id) throws PlanetNotFoundException, SwapiUnreachableException {

        Planet planet = orikaMapper.map(requestPlanetDTO, Planet.class);
        planet.setId(id);
        planet = planetService.updatePlanet(planet);

        ResponsePlanetDTO responsePlanetDTO =  orikaMapper.map(planet, ResponsePlanetDTO.class);

        return ResponseEntity.status(HttpStatus.OK).body(responsePlanetDTO);
    }

    @PostMapping(produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ResponsePlanetDTO> createPlanet(@Valid @RequestBody RequestPlanetDTO requestPlanetDTO) throws PlanetNotFoundException, SwapiUnreachableException {
        Planet planet = orikaMapper.map(requestPlanetDTO, Planet.class);

        planet = planetService.createPlanet(planet);

        ResponsePlanetDTO responsePlanetDTO =  orikaMapper.map(planet, ResponsePlanetDTO.class);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .replacePath("/{id}")
                            .buildAndExpand(responsePlanetDTO.getId()).toUri();

        return ResponseEntity.created(location).body(responsePlanetDTO);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePlanet(@PathVariable String id) {
        planetService.deletePlanetById(id);
    }

}
