package com.pedrogonic.swapi.services;

import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.dtos.SwapiPlanetDTO;

import java.util.List;

public interface ISwapiService {

    /**
     * Calls the SWAPI for a planet with the given name
     * <p>
     * If no planet is found, the api returns an empty list with a status of 200 (OK).
     * </p>
     * @param name - must not be null.
     * @return a matching planet.
     */
    Planet findPlanetByName(String name) throws PlanetNotFoundException;

    /**
     * Returns all planets from the SWAPI
     * <p>
     * This method should be used with a find all in this api. It will return a list with all recorded planets, which
     * should be gone through to find the film count in each planet in local database.
     * </p>
     * @return list of all planets.
     */
    List<Planet> findAll();

}
