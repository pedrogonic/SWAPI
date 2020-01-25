package com.pedrogonic.swapi.repositories;

import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository
public interface IPlanetRepository {

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
    List<Planet> findAll(Pageable pageable, PlanetFilter planetFilter);

    /**
     * TODO
     * @param id
     * @return
     */
    Optional<Planet> findById(Object id);

    /**
     * TODO
     * @param dbPlanetDTO
     * @return
     */
    Planet save(Planet dbPlanetDTO);

    /**
     * TODO
     * @param dbPlanetDTO
     * @return
     */
    Planet insert(Planet dbPlanetDTO);

    /**
     * TODO
     * @param id
     */
    void deleteById(Object id);

}
