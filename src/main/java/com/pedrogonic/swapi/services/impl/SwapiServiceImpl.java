package com.pedrogonic.swapi.services.impl;

import com.pedrogonic.swapi.application.components.Messages;
import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.application.exception.SwapiUnreachableException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.swapi.SwapiPlanetDTO;
import com.pedrogonic.swapi.model.dtos.swapi.SwapiSearchDTO;
import com.pedrogonic.swapi.services.ISwapiService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Log4j2
@Service
public class SwapiServiceImpl implements ISwapiService {

    private final String swapiPlanetsUri = "https://swapi.co/api/planets";
    private final String queryParam = "search";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrikaMapper orikaMapper;

    @Autowired
    Messages messages;

    @Override
    public Planet findPlanetByName(String name) throws PlanetNotFoundException, SwapiUnreachableException {

        List<SwapiPlanetDTO> swapiPlanetDTOs = callApi(name);

        if (swapiPlanetDTOs.size() == 0)
            throw new PlanetNotFoundException(messages.getErrorPlanetNotFoundInSwapi(name));

        return convertSwapiPlanetDTOToPlanet(swapiPlanetDTOs.get(0));

    }

    @Override
    @Cacheable(cacheNames = "planets")
    public List<Planet> findAll() throws SwapiUnreachableException {
        List<SwapiPlanetDTO> swapiPlanetDTOs = callApi();

        List<Planet> planets = new ArrayList<>();
        swapiPlanetDTOs.forEach(swapiPlanetDTO ->  planets.add(convertSwapiPlanetDTOToPlanet(swapiPlanetDTO)) );

        return planets;
    }

    private Planet convertSwapiPlanetDTOToPlanet(SwapiPlanetDTO swapiPlanetDTO) {
        Planet planet = orikaMapper.map(swapiPlanetDTO, Planet.class);
        planet.setFilmCount(swapiPlanetDTO.getFilms().size());

        return planet;
    }

    /**
     * Calls method callApi for findAll()
     * @return list of planets
     */
    private List<SwapiPlanetDTO> callApi() throws SwapiUnreachableException { return callApi(null); }

    /**
     * Method that calls api
     * <p>
     * To get all results, pass a null String
     * </p>
     * @param name - must be null to FindAll, otherwise, valid name of Planet
     * @return list of planets
     */
    private List<SwapiPlanetDTO> callApi(String name) throws SwapiUnreachableException {


        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(swapiPlanetsUri);

        if (name != null)
            uriBuilder.queryParam(queryParam, name);

        List<SwapiPlanetDTO> results = new ArrayList<>();

        SwapiSearchDTO swapiSearchDTO = SwapiSearchDTO.builder().next(uriBuilder.build().toString()).build();

        try {
            while (swapiSearchDTO.getNext() != null) {
                log.info("Calling SWAPI URI: " + swapiSearchDTO.getNext());
                swapiSearchDTO = restTemplate.getForObject(swapiSearchDTO.getNext(), SwapiSearchDTO.class);
                results.addAll(swapiSearchDTO.getResults());
            }
        } catch (final HttpServerErrorException e) {
            // Throwing error if SWAPI is unreachable by any reason. (E.g.: 500 Internal Server Error, 429 Too Many Requests , ...
            throw new SwapiUnreachableException(messages.getErrorSwapiUnreachable(e.getStatusCode().toString()));
        }

        results.sort(Comparator.comparing(SwapiPlanetDTO::getName));

        return results;
    }

}
