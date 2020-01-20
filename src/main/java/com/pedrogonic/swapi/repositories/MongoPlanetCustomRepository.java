package com.pedrogonic.swapi.repositories;

import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.model.mongo.MongoPlanet;

import java.util.List;

public interface MongoPlanetCustomRepository {

    List<MongoPlanet> findAll(PlanetFilter planetFilter);

}
