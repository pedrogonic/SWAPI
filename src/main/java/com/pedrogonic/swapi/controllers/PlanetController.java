package com.pedrogonic.swapi.controllers;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.PlanetDTO;
import com.pedrogonic.swapi.services.IPlanetService;
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
public class PlanetController {

    @Autowired
    IPlanetService planetService;

    @Autowired
    OrikaMapper orikaMapper;


    @GetMapping(value = "")
    List<PlanetDTO> listAll(@RequestParam(required = false) final String name) {
        // service.find with filter

        // TODO
        List<Planet> planets = planetService.findAll();

        return null;
    }

    // TODO
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    PlanetDTO getById(@PathVariable final String id) {
        // service.findById

        Planet planet = planetService.findById(id);

        // if found: 200

        // if not found: 404

        return null;
    }

    // TODO
    @PutMapping(value = "/{id}")
    ResponseEntity<?> updatePlanet(@Valid @RequestBody PlanetDTO planetDTO, @PathVariable String id) {
        // service.update

        Planet planet = null; // mapper
        // planet.setId(id);

        planet = planetService.updatePlanet(planet);

        // if created: 201

        // if updated: 200

        return null; // mapper
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createPlanet(@Valid @RequestBody PlanetDTO planetDTO) {
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
