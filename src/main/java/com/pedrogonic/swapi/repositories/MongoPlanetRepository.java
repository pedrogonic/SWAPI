package com.pedrogonic.swapi.repositories;

import com.pedrogonic.swapi.domain.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoPlanetRepository extends MongoRepository<Planet, String>, IPlanetRepository {
}
