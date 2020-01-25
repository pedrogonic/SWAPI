package com.pedrogonic.swapi.model.dtos.swapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiSearchDTO {

    private String name;

    private String next;

    private List<SwapiPlanetDTO> results;

}
