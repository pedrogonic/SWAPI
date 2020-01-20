package com.pedrogonic.swapi.repositories;

import com.pedrogonic.swapi.domain.Planet;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IPlanetRepository extends PagingAndSortingRepository<Planet, String> {
}
