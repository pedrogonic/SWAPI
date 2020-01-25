package com.pedrogonic.swapi.controllers;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.application.exception.SwapiUnreachableException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.http.ResponsePlanetDTO;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.model.dtos.db.mongo.MongoPlanet;
import com.pedrogonic.swapi.services.IPlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    List<ResponsePlanetDTO> listAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                    @RequestParam(required = false) final String name,
                                    @RequestParam(required = false) final String id) throws SwapiUnreachableException { // TODO: Remove ID

        Pageable pageable = PageRequest.of(page, size);

        PlanetFilter planetFilter = PlanetFilter.builder()
                .id(id)
                .name(name)
                .build();

        List<Planet> planets = planetService.findAll(pageable, planetFilter);

        return orikaMapper.mapAsList(planets, ResponsePlanetDTO.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponsePlanetDTO getById(@PathVariable final String id) throws PlanetNotFoundException, SwapiUnreachableException {


        Planet planet = planetService.findById(id);

        return orikaMapper.map(planet, ResponsePlanetDTO.class);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updatePlanet(@Valid @RequestBody ResponsePlanetDTO responsePlanetDTO, @PathVariable String id) throws PlanetNotFoundException, SwapiUnreachableException {

        responsePlanetDTO.setId(id);

        Planet planet = orikaMapper.map(responsePlanetDTO, Planet.class);

        planet = planetService.updatePlanet(planet);

        responsePlanetDTO =  orikaMapper.map(planet, ResponsePlanetDTO.class);

        return ResponseEntity.status(HttpStatus.OK).body(responsePlanetDTO);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createPlanet(@Valid @RequestBody ResponsePlanetDTO responsePlanetDTO) throws PlanetNotFoundException, SwapiUnreachableException {
        // TODO RequestPlanet
        responsePlanetDTO.setId(null);

        Planet planet = orikaMapper.map(responsePlanetDTO, Planet.class);

        planet = planetService.createPlanet(planet);

        responsePlanetDTO =  orikaMapper.map(planet, ResponsePlanetDTO.class);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .replacePath("/{id}")
                            .buildAndExpand(responsePlanetDTO.getId()).toUri();

        return ResponseEntity.created(location).body(responsePlanetDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePlanet(@PathVariable String id) {
        planetService.deletePlanetById(id);
    }

}
