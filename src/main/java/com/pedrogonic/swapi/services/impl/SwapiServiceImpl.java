package com.pedrogonic.swapi.services.impl;

import com.pedrogonic.swapi.application.components.Messages;
import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.application.exception.SwapiUnreachableException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.swapi.SwapiPlanetDTO;
import com.pedrogonic.swapi.services.ISwapiService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SwapiServiceImpl implements ISwapiService {

    private final String queryParam = "search";

    private final OrikaMapper orikaMapper;

    private final Messages messages;

    private final SwapiApiCacheableCaller swapiApiCacheableCaller;

    public SwapiServiceImpl(OrikaMapper orikaMapper, Messages messages, SwapiApiCacheableCaller swapiApiCacheableCaller) {
        this.orikaMapper = orikaMapper;
        this.messages = messages;
        this.swapiApiCacheableCaller = swapiApiCacheableCaller;
    }

    @Override
    public Planet findPlanetByName(String name) throws PlanetNotFoundException, SwapiUnreachableException {

        List<SwapiPlanetDTO> swapiPlanetDTOs = callApi(name);

        if (swapiPlanetDTOs.size() == 0)
            throw new PlanetNotFoundException(messages.getErrorPlanetNotFoundInSwapi(name));

        return convertSwapiPlanetDTOToPlanet(swapiPlanetDTOs.get(0));

    }

    @Override
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

        Map<String, String> params = new HashMap<>();
        if (name != null)
            params.put(queryParam, name);


        List<SwapiPlanetDTO> planets;

//        try {
            planets = swapiApiCacheableCaller.getAll(params);
//        } catch (HttpServerErrorException e) {
//            throw new SwapiUnreachableException(messages.getErrorSwapiUnreachable(e.getStatusCode().toString()));
//        }

        return planets;
    }

}
