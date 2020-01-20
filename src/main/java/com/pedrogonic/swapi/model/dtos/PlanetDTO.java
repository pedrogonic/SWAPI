package com.pedrogonic.swapi.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanetDTO {


    private String id;

    @NotBlank(message = "{planet.name.NotBlank}")
    private String name;

    @NotBlank(message = "{planet.climate.NotBlank}")
    private String climate;

    @NotBlank(message = "{planet.terrain.NotBlank}")
    private String terrain;


    private int filmCount;

}
