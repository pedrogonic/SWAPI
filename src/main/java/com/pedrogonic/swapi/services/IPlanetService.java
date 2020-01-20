package com.pedrogonic.swapi.services;

import com.pedrogonic.swapi.domain.Planet;

import java.util.List;

public interface IPlanetService {

    List<Planet> findAll(/* Filter */);
    Planet findById(String id) throws Exception;
    Planet updatePlanet(Planet planet);
    Planet createPlanet(Planet planet);
    void deletePlanetById(String id);

}
