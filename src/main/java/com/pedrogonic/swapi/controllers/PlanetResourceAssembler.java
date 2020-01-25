package com.pedrogonic.swapi.controllers;

import com.pedrogonic.swapi.model.dtos.http.ResponsePlanetDTO;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlanetResourceAssembler implements RepresentationModelAssembler<ResponsePlanetDTO, EntityModel<ResponsePlanetDTO>> {


    @SneakyThrows
    @Override
    public EntityModel<ResponsePlanetDTO> toModel(ResponsePlanetDTO entity) {
        return new EntityModel<ResponsePlanetDTO>(entity,
                linkTo(methodOn(PlanetController.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(PlanetController.class).listAll()).withRel("planets")
        );
    }
}
