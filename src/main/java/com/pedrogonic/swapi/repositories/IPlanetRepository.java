package com.pedrogonic.swapi.repositories;

import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
     * Returns planet with provided id
     * @param id
     * @return updated planet
     */
    Optional<Planet> findById(Object id);

    /**
     * Updates planet in database with same id
     * <p>
     * Saves an existing planet. Planet passed to this must must contain a valid id to be matched with object in
     * database.
     * </p>
     * @param planet - must not be null. must have valid id.
     * @return saved planet
     */
    Planet save(Planet planet);

    /**
     * Inserts a new planet
     * @param planet - must not be null.
     * @return inserted planet
     */
    Planet insert(Planet planet);

    /**
     * Deletes planet with given id
     * @param id - must not be null
     */
    void deleteById(Object id);

}
