package com.pedrogonic.swapi.services.impl;

import com.pedrogonic.swapi.application.components.Messages;
import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.SwapiPlanetDTO;
import com.pedrogonic.swapi.model.dtos.SwapiSearchDTO;
import com.pedrogonic.swapi.services.ISwapiService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Log4j2
@Service
public class SwapiServiceImpl implements ISwapiService {

    public final String SWAPI_PLANETS_URI = "https://swapi.co/api/planets";
    public final String QUERY_PARAM = "search";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrikaMapper orikaMapper;

    @Autowired
    Messages messages;

    @Override
    public Planet findPlanetByName(String name) throws PlanetNotFoundException {

        List<SwapiPlanetDTO> swapiPlanetDTOs;

            swapiPlanetDTOs = callApi(name);

        if (swapiPlanetDTOs.size() == 0)
            throw new PlanetNotFoundException(messages.getErrorPlanetNotFoundInSwapi(name));

        return convertSwapiPlanetDTOToPlanet(swapiPlanetDTOs.get(0));

    }

    @Override
    public List<Planet> findAll() {
        List<SwapiPlanetDTO> swapiPlanetDTOs = callApi(null);

        List<Planet> planets = new ArrayList<>();
        swapiPlanetDTOs.forEach(swapiPlanetDTO -> { planets.add(convertSwapiPlanetDTOToPlanet(swapiPlanetDTO)); });

        return planets;
    }

    private Planet convertSwapiPlanetDTOToPlanet(SwapiPlanetDTO swapiPlanetDTO) {
        Planet planet = orikaMapper.map(swapiPlanetDTO, Planet.class);
        planet.setFilmCount(swapiPlanetDTO.getFilms().size());

        return planet;
    }

    /**
     * Method that calls api
     * <p>
     * To get all results, pass a null String
     * </p>
     * @param name - must be null to FindAll, otherwise, valid name of Planet
     * @return list of planets
     */
    private List<SwapiPlanetDTO> callApi(String name) {


        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(SWAPI_PLANETS_URI);

        if (name != null)
            uriBuilder.queryParam(QUERY_PARAM, name);

        // TODO: check for STATUS != 200

        List<SwapiPlanetDTO> results = new ArrayList<>();

        SwapiSearchDTO swapiSearchDTO = SwapiSearchDTO.builder().next(uriBuilder.build().toString()).build();

        while(swapiSearchDTO.getNext() != null) {
            swapiSearchDTO = restTemplate.getForObject(swapiSearchDTO.getNext(), SwapiSearchDTO.class);
            results.addAll(swapiSearchDTO.getResults());
        }

        results.sort(Comparator.comparing(SwapiPlanetDTO::getName));

        return results;
    }

}
