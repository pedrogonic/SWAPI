package com.pedrogonic.swapi.controllers;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.application.exception.SwapiUnreachableException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.PlanetDTO;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.model.mongo.MongoPlanet;
import com.pedrogonic.swapi.services.IPlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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


    @GetMapping("")
    List<PlanetDTO> listAll(@PageableDefault(size = Integer.MAX_VALUE, sort = MongoPlanet.FIELD_NAME) final Pageable pageable, //TODO: Remove reference to Mongo
                            @RequestParam(required = false) final String name,
                            @RequestParam(required = false) final String id) throws SwapiUnreachableException { // TODO: Remove ID

        PlanetFilter planetFilter = PlanetFilter.builder()
                .id(id)
                .name(name)
                .build();

        List<Planet> planets = planetService.findAll(pageable, planetFilter);

        return orikaMapper.mapAsList(planets, PlanetDTO.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    PlanetDTO getById(@PathVariable final String id) throws PlanetNotFoundException, SwapiUnreachableException {
        Planet planet = planetService.findById(id);

        return orikaMapper.map(planet, PlanetDTO.class);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updatePlanet(@Valid @RequestBody PlanetDTO planetDTO, @PathVariable String id) throws PlanetNotFoundException, SwapiUnreachableException {

        planetDTO.setId(id);

        Planet planet = orikaMapper.map(planetDTO, Planet.class);

        planet = planetService.updatePlanet(planet);

        planetDTO =  orikaMapper.map(planet, PlanetDTO.class);

        return ResponseEntity.status(HttpStatus.OK).body(planetDTO);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createPlanet(@Valid @RequestBody PlanetDTO planetDTO) throws PlanetNotFoundException, SwapiUnreachableException {
        planetDTO.setId(null);

        Planet planet = orikaMapper.map(planetDTO, Planet.class);

        planet = planetService.createPlanet(planet);

        planetDTO =  orikaMapper.map(planet, PlanetDTO.class);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .replacePath("/{id}")
                            .buildAndExpand(planetDTO.getId()).toUri();

        return ResponseEntity.created(location).body(planetDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePlanet(@PathVariable String id) {
        planetService.deletePlanetById(id);
    }

}
