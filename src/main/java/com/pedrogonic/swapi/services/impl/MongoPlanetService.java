package com.pedrogonic.swapi.services.impl;

import com.pedrogonic.swapi.application.components.OrikaMapper;
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
    public List<Planet> findAll(/* Filter */) {
        return null;
    }

    @Override
    public Planet findById(String id) {
        return null;
    }

    @Override
    public Planet updatePlanet(Planet planet) {
        return null;
    }

    @Override
    public Planet createPlanet(Planet planet) {
        MongoPlanet mongoPlanet = orikaMapper.map(planet, MongoPlanet.class);
        mongoPlanet = mongoPlanetRepository.save(mongoPlanet);
        return orikaMapper.map(mongoPlanet, Planet.class);
    }

    @Override
    public void deletePlanetById(String id) {
        mongoPlanetRepository.deleteById(new ObjectId(id));
    }
}
