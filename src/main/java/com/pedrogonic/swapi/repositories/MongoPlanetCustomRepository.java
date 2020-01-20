package com.pedrogonic.swapi.repositories;

import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.model.mongo.MongoPlanet;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MongoPlanetCustomRepository {

    List<MongoPlanet> findAll(Pageable pageable, PlanetFilter planetFilter);

}
