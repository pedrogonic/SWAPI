package com.pedrogonic.swapi.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanetDTO {


    private String id;


    private String name;


    private String climate;


    private String terrain;


    private int filmCount;

}
