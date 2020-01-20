package com.pedrogonic.swapi.controllers;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.PlanetDTO;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.services.IPlanetService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/planets")
@Log4j2
public class PlanetController {

    @Autowired
    IPlanetService planetService;

    @Autowired
    OrikaMapper orikaMapper;


    @GetMapping(value = "")
    List<PlanetDTO> listAll(@RequestParam(required = false) final String name,
                            @RequestParam(required = false) final String id) { // TODO: Pageable

        PlanetFilter planetFilter = PlanetFilter.builder()
                .id(id)
                .name(name)
                .build();

        List<Planet> planets = planetService.findAll(planetFilter);

        return orikaMapper.mapAsList(planets, PlanetDTO.class);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    PlanetDTO getById(@PathVariable final String id) throws PlanetNotFoundException {
        Planet planet = planetService.findById(id);

        return orikaMapper.map(planet, PlanetDTO.class);
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<?> updatePlanet(@Valid @RequestBody PlanetDTO planetDTO, @PathVariable String id) {
        planetDTO.setId(id);

        Planet planet = orikaMapper.map(planetDTO, Planet.class);

        planet = planetService.updatePlanet(planet);

        planetDTO =  orikaMapper.map(planet, PlanetDTO.class);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/{id}")
                .buildAndExpand(planetDTO.getId()).toUri();

        // if created: 201
        if (planet.getId().equals(id))
            return ResponseEntity.status(HttpStatus.OK).body(planetDTO);

        // if updated: 200
        else
            return ResponseEntity.created(location).body(planetDTO);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createPlanet(@Valid @RequestBody PlanetDTO planetDTO) {
        planetDTO.setId(null);

        Planet planet = orikaMapper.map(planetDTO, Planet.class);

        planet = planetService.createPlanet(planet);

        planetDTO =  orikaMapper.map(planet, PlanetDTO.class);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .replacePath("/{id}")
                            .buildAndExpand(planetDTO.getId()).toUri();

        return ResponseEntity.created(location).body(planetDTO);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePlanet(@PathVariable String id) {
        planetService.deletePlanetById(id);
    }

}
