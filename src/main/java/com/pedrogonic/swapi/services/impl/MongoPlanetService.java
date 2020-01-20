package com.pedrogonic.swapi.services.impl;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.utils.ConversionUtils;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.mongo.MongoPlanet;
import com.pedrogonic.swapi.repositories.MongoPlanetRepository;
import com.pedrogonic.swapi.services.IPlanetService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoPlanetService implements IPlanetService {

    @Autowired
    MongoPlanetRepository mongoPlanetRepository;

    @Autowired
    OrikaMapper orikaMapper;

    @Override
    public List<Planet> findAll(/* Filter, Pageable */) { // TODO: Filter, Pageable
        return orikaMapper.mapAsList(mongoPlanetRepository.findAll(), Planet.class);
    }

    @Override
    public Planet findById(String id) throws Exception {
        ObjectId objectId = ConversionUtils.stringToObjectId(id);

        MongoPlanet mongoPlanet = mongoPlanetRepository.findById(objectId)
                .orElseThrow(() -> new Exception()); // TODO: Custom Exception

        // TODO: Call SWAPI

        return orikaMapper.map(mongoPlanet, Planet.class);
    }

    @Override
    public Planet updatePlanet(Planet planet) {

        // Using utils method to convert String id to a valid ObjectId
        // or a new ObjectId(), and then converting back to String.
        // This ensures that the mapper won't fail with an Illegal Argument Exception
        planet.setId(ConversionUtils.stringToObjectId(planet.getId()).toString());

        MongoPlanet mongoPlanet = orikaMapper.map(planet, MongoPlanet.class);

        mongoPlanet = mongoPlanetRepository.save(mongoPlanet);
        return orikaMapper.map(mongoPlanet, Planet.class);
    }

    @Override
    public Planet createPlanet(Planet planet) {

        MongoPlanet mongoPlanet = orikaMapper.map(planet, MongoPlanet.class);
        mongoPlanet = mongoPlanetRepository.insert(mongoPlanet);

        return orikaMapper.map(mongoPlanet, Planet.class);
    }

    @Override
    public void deletePlanetById(String id) {
        ObjectId objectId = ConversionUtils.stringToObjectId(id);
        mongoPlanetRepository.deleteById(objectId);
    }
}
