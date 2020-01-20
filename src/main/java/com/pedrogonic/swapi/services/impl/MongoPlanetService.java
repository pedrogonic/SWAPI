package com.pedrogonic.swapi.services.impl;

import com.pedrogonic.swapi.application.components.Messages;
import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.application.exception.SwapiUnreachableException;
import com.pedrogonic.swapi.application.utils.ConversionUtils;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.model.mongo.MongoPlanet;
import com.pedrogonic.swapi.repositories.MongoPlanetRepository;
import com.pedrogonic.swapi.services.IPlanetService;
import com.pedrogonic.swapi.services.ISwapiService;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class MongoPlanetService implements IPlanetService {

    @Autowired
    MongoPlanetRepository mongoPlanetRepository;

    @Autowired
    ISwapiService swapiService;

    @Autowired
    OrikaMapper orikaMapper;

    @Autowired
    Messages messages;

    @Override
    public List<Planet> findAll(Pageable pageable, PlanetFilter planetFilter) throws SwapiUnreachableException {

        List<MongoPlanet> mongoPlanets;

        mongoPlanets = mongoPlanetRepository.findAll(pageable, planetFilter);

        List<Planet> planets = orikaMapper.mapAsList(mongoPlanets, Planet.class);

        switch (mongoPlanets.size()) {
            case 1:
                try {

                    planets.get(0).setFilmCount( swapiService.findPlanetByName( planets.get(0).getName() ) .getFilmCount() );

                } catch (PlanetNotFoundException e) {
                    // Only happens if Planet is included directly in the database
                    // since the api checks against the original SWAPI when creating
                    // a new planet.
                    log.error(e.getMessage());
                }
                break;
            case 0: break;
            default:
                List<Planet> swapiPlanets = swapiService.findAll();

                planets = planets.stream().map(
                        planet -> {
                            Planet swapiPlanet = swapiPlanets.stream().filter(
                                    sp -> sp.getName().equals(planet.getName())
                                            ).findFirst().orElse(new Planet());
                            planet.setFilmCount(swapiPlanet.getFilmCount());
                            return planet;
                        }
                ).collect(Collectors.toList());

                break;
        }

        return planets;
    }

    @Override
    public Planet findById(String id) throws PlanetNotFoundException, SwapiUnreachableException {
        ObjectId objectId = ConversionUtils.stringToObjectId(id);

        MongoPlanet mongoPlanet = mongoPlanetRepository.findById(objectId)
                .orElseThrow(() -> new PlanetNotFoundException(messages.getErrorPlanetNotFoundById(id)));

        Planet planet = orikaMapper.map(mongoPlanet, Planet.class);
        planet.setFilmCount(swapiService.findPlanetByName(mongoPlanet.getName()).getFilmCount());

        return planet;
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
    public Planet createPlanet(Planet planet) throws PlanetNotFoundException, SwapiUnreachableException {

        // Call api to check if planet exists. If not, throw an Exception.
        int filmCount = swapiService.findPlanetByName(planet.getName()).getFilmCount();

        MongoPlanet mongoPlanet = orikaMapper.map(planet, MongoPlanet.class);
        mongoPlanet = mongoPlanetRepository.insert(mongoPlanet);

        planet = orikaMapper.map(mongoPlanet, Planet.class);
        planet.setFilmCount(filmCount);

        return planet;
    }

    @Override
    public void deletePlanetById(String id) {
        ObjectId objectId = ConversionUtils.stringToObjectId(id);
        mongoPlanetRepository.deleteById(objectId);
    }
}
