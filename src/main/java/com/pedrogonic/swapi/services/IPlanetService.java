package com.pedrogonic.swapi.services;

import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPlanetService {

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
     * Return planet with provided Id
     * <p>
     * If no planet is found, throws  PlanetNotFoundException.
     * </p>
     * @param id - must not be null
     * @return Planet object with the given id
     * @throws PlanetNotFoundException - if no planet is found
     */
    Planet findById(String id) throws PlanetNotFoundException;

    /**
     * Updates an existing Planet
     * <p>
     * TODO: decide update behavior
     * </p>
     * @param planet - must not be null
     * @return persisted Planet
     */
    Planet updatePlanet(Planet planet);

    /**
     * Creates a Planet
     * <p>
     * TODO: decide create behavior
     * </p>
     * @param planet - must not be null
     * @return
     * @throws PlanetNotFoundException - if no planet is found in the original SWAPI
     */
    Planet createPlanet(Planet planet) throws PlanetNotFoundException;

    /**
     * Deletes a Planet by Id
     * <p>
     * Deletes the entity with the given id.
     * </p>
     * <p>
     * If no entity is found with this id, it does nothing
     * </p>
     * @param id - must not be null
     */
    void deletePlanetById(String id);

}
