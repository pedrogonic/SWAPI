package com.pedrogonic.swapi.controllers;

import com.pedrogonic.swapi.model.dtos.PlanetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/planets")
public class PlanetController {

    // @Autowired
    // Service service;


    // TODO
    @GetMapping(value = "/")
    List<PlanetDTO> listAll(@RequestParam(required = false) final String name) {
        // service.find with filter

        return null;
    }

    // TODO
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    PlanetDTO getById(@PathVariable final String id) {
        // service.findById

        // if found: 200

        // if not found: 404

        return null;
    }

    // TODO
    @PutMapping(value = "/{id}")
    ResponseEntity<?> updatePlanet(@Valid @RequestBody PlanetDTO planet, @PathVariable String id) {
        // service.update

        // if created: 201

        // if updated: 200

        return null;
    }

    // TODO
    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createPlanet(@Valid @RequestBody PlanetDTO planet) {
        // service.create
        return null;
    }

    // TODO
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePlanet(@PathVariable String id) {
        // service.delete
    }

}
