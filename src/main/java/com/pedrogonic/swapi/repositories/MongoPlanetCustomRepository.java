package com.pedrogonic.swapi.repositories;

import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.model.mongo.MongoPlanet;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MongoPlanetCustomRepository {

    /**
     * Returns planets that matches the provided filter or, if not provided, a sorted list
     * <p>
     * If a filled Planet Filter is provided, this method searches the repository for planets that match the Name and
     * Id fields in the filter.
     * </p>
     * <p>
     * If both fields in the Filter are null, it finds all records by the Pageable object.
     * </p>
     * @param pageable - must not be null
     * @param planetFilter - must not be null
     * @return List of Planets matching the filter or restricted list by Pageable object
     */
    List<MongoPlanet> findAll(Pageable pageable, PlanetFilter planetFilter);

}
