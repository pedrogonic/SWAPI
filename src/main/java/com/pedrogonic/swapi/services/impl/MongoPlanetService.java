package com.pedrogonic.swapi.services.impl;

import com.pedrogonic.swapi.application.components.Messages;
import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.application.utils.ConversionUtils;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.model.mongo.MongoPlanet;
import com.pedrogonic.swapi.repositories.MongoPlanetRepository;
import com.pedrogonic.swapi.services.IPlanetService;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class MongoPlanetService implements IPlanetService {

    @Autowired
    MongoPlanetRepository mongoPlanetRepository;

    @Autowired
    OrikaMapper orikaMapper;

    @Autowired
    Messages messages;

    @Override
    public List<Planet> findAll(Pageable pageable, PlanetFilter planetFilter) {

        List<MongoPlanet> mongoPlanets;

        mongoPlanets = mongoPlanetRepository.findAll(pageable, planetFilter);

        return orikaMapper.mapAsList(mongoPlanets, Planet.class);
    }

    @Override
    public Planet findById(String id) throws PlanetNotFoundException {
        ObjectId objectId = ConversionUtils.stringToObjectId(id);

        MongoPlanet mongoPlanet = mongoPlanetRepository.findById(objectId)
                .orElseThrow(() -> new PlanetNotFoundException(messages.getErrorPlanetNotFoundById(id)));

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
