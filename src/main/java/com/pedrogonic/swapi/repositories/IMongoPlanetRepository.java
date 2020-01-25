package com.pedrogonic.swapi.repositories;

import com.pedrogonic.swapi.model.dtos.db.mongo.MongoPlanet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IMongoPlanetRepository extends MongoRepository<MongoPlanet, ObjectId> {
}