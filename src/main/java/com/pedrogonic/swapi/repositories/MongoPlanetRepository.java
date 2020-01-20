package com.pedrogonic.swapi.repositories;

import com.pedrogonic.swapi.model.mongo.MongoPlanet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoPlanetRepository extends MongoRepository<MongoPlanet, ObjectId>, MongoPlanetCustomRepository {
}
