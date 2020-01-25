package com.pedrogonic.swapi.model.dtos.swapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiPlanetDTO {

    private String name;
    private List<String> films;

}
