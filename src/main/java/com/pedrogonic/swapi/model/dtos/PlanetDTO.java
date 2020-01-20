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

    @NotBlank
    private String name;

    @NotBlank
    private String climate;

    @NotBlank
    private String terrain;


    private int filmCount;

}
