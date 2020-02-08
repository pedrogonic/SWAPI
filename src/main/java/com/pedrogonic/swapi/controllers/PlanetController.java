package com.pedrogonic.swapi.controllers;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.application.exception.SwapiUnreachableException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.http.RequestPlanetDTO;
import com.pedrogonic.swapi.model.dtos.http.ResponsePlanetDTO;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.services.IPlanetService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/planets")
public class PlanetController {

    private final IPlanetService planetService;

    private final OrikaMapper orikaMapper;

    private final PlanetResourceAssembler assembler;

    public PlanetController(IPlanetService planetService, OrikaMapper orikaMapper, PlanetResourceAssembler assembler) {
        this.planetService = planetService;
        this.orikaMapper = orikaMapper;
        this.assembler = assembler;
    }


    @GetMapping(produces = "application/json; charset=UTF-8")
    CollectionModel<EntityModel<ResponsePlanetDTO>> listAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                @RequestParam(required = false) final String name) throws SwapiUnreachableException {

        Pageable pageable = PageRequest.of(page, size);

        PlanetFilter planetFilter = PlanetFilter.builder()
                .name(name)
                .build();

        List<Planet> planets = planetService.findAll(pageable, planetFilter);
        List<ResponsePlanetDTO> planetDTOList = orikaMapper.mapAsList(planets, ResponsePlanetDTO.class);
        List<EntityModel<ResponsePlanetDTO>> resources = planetDTOList.stream()
                .map(assembler::toModel).collect(Collectors.toList());

        return new CollectionModel<>(resources,
                linkTo(methodOn(PlanetController.class).listAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    EntityModel<ResponsePlanetDTO> getById(@PathVariable final String id) throws PlanetNotFoundException, SwapiUnreachableException {

        Planet planet = planetService.findById(id);
        ResponsePlanetDTO responsePlanetDTO = orikaMapper.map(planet, ResponsePlanetDTO.class);

        return assembler.toModel(responsePlanetDTO);
    }

    @PutMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    ResponseEntity<EntityModel<ResponsePlanetDTO>> updatePlanet(@Valid @RequestBody RequestPlanetDTO requestPlanetDTO, @PathVariable String id)
                                                                    throws PlanetNotFoundException, SwapiUnreachableException {

        Planet planet = orikaMapper.map(requestPlanetDTO, Planet.class);
        planet.setId(id);
        planet = planetService.updatePlanet(planet);

        ResponsePlanetDTO responsePlanetDTO =  orikaMapper.map(planet, ResponsePlanetDTO.class);

        EntityModel<ResponsePlanetDTO> resource = assembler.toModel(responsePlanetDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/{id}")
                .buildAndExpand(resource.getContent().getId()).toUri();

        return ResponseEntity.ok().location(location).body(resource);
    }

    @PostMapping(produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<ResponsePlanetDTO>> createPlanet(@Valid @RequestBody RequestPlanetDTO requestPlanetDTO) throws PlanetNotFoundException, SwapiUnreachableException {
        Planet planet = orikaMapper.map(requestPlanetDTO, Planet.class);

        planet = planetService.createPlanet(planet);

        ResponsePlanetDTO responsePlanetDTO =  orikaMapper.map(planet, ResponsePlanetDTO.class);

        EntityModel<ResponsePlanetDTO> resource = assembler.toModel(responsePlanetDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .replacePath("/{id}")
                            .buildAndExpand(resource.getContent().getId()).toUri();


        return ResponseEntity.created(location).body(resource);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePlanet(@PathVariable String id) {
        planetService.deletePlanetById(id);
    }

    /**
     * Protected method for HATEOAS Resource Assembler
     * @return
     * @throws SwapiUnreachableException
     */
    protected CollectionModel<EntityModel<ResponsePlanetDTO>> listAll() throws SwapiUnreachableException {
        return listAll(0, 10, null);
    }
}
